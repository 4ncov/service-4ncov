package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.Constants;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.common.exception.UserNotFoundException;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.controller.resp.user.SignInResponse;
import com.ncov.module.controller.resp.user.UserDetailResponse;
import com.ncov.module.controller.resp.user.UserResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import com.ncov.module.mapper.SupplierMapper;
import com.ncov.module.mapper.UserInfoMapper;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Service("userInfoService")
@Slf4j
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfoEntity> {

    private final UserInfoMapper userInfoMapper;
    private final HospitalInfoMapper hospitalInfoMapper;
    private final SupplierMapper supplierMapper;
    private final String jwtSecret;
    private final Long jwtExpirationInMs;

    public UserInfoService(UserInfoMapper userInfoMapper,
                           HospitalInfoMapper hospitalInfoMapper,
                           SupplierMapper supplierMapper,
                           @Value("${security.jwtSecret}") String jwtSecret,
                           @Value("${security.jwtExpirationInMs}") Long jwtExpirationInMs) {
        this.userInfoMapper = userInfoMapper;
        this.hospitalInfoMapper = hospitalInfoMapper;
        this.supplierMapper = supplierMapper;
        this.jwtSecret = jwtSecret;
        this.jwtExpirationInMs = jwtExpirationInMs;
    }

    public UserInfoEntity createUniqueUser(UserInfoEntity user) {
        Integer existingUserCount = userInfoMapper.selectCountByPhoneOrNickName(
                user.getUserPhone(), user.getUserNickName());
        if (existingUserCount > 0) {
            log.warn("User already exist, phone=[{}], nickName=[{}]", user.getUserPhone(), user.getUserNickName());
            throw new DuplicateException("该机构已有注册账号，不可重复注册");
        }
        save(user);
        return user;
    }

    public SignInResponse signIn(String telephone, String password) {
        log.info("Signing in, phone=[{}]", telephone);
        UserInfoEntity user = Optional.ofNullable(userInfoMapper.findByUserPhone(telephone))
                .orElseThrow(() -> new BadCredentialsException("User not exist"));
        if (!user.getUserPasswordSHA256().equals(DigestUtils.sha256Hex(password))) {
            log.warn("Password is incorrect, phone=[{}]", telephone);
            throw new BadCredentialsException("Incorrect password");
        }

        JwtBuilder jwtBuilder = new DefaultJwtBuilder()
                .signWith(SignatureAlgorithm.HS256, jwtSecret);

        addOrganisationClaimToJwt(user, jwtBuilder);

        Date now = new Date();
        Date jwtExpirationTime = new Date(now.getTime() + jwtExpirationInMs);
        UserRole role = UserRole.getRoleById(user.getUserRoleId());
        String token = jwtBuilder
                .setExpiration(jwtExpirationTime)
                .setId(UUID.randomUUID().toString())
                .setIssuer(Constants.JWT_ISSUER)
                .setIssuedAt(now)
                .claim("userRole", role)
                .claim("userNickName", user.getUserNickName())
                .claim("id", user.getId())
                .compact();
        return SignInResponse.builder().token(token).expiresAt(jwtExpirationTime).role(role.name()).build();
    }

    public com.ncov.module.controller.resp.Page<UserResponse> listAllUsers(Integer page, Integer size) {
        Page<UserInfoEntity> results = userInfoMapper.selectPage(
                new Page<UserInfoEntity>().setCurrent(page).setSize(size), null);
        return com.ncov.module.controller.resp.Page.<UserResponse>builder()
                .total(results.getTotal())
                .page(page)
                .pageSize(size)
                .data(results.getRecords().stream().map(this::toResponse).collect(Collectors.toList()))
                .build();
    }

    public UserDetailResponse getDetail(Long id) {
        UserInfoEntity user = getUser(id);
        HospitalInfoEntity hospital = null;
        SupplierInfoEntity supplier = null;
        if (user.isHospital()) {
            hospital = hospitalInfoMapper.selectByHospitalCreatorUserId(user.getId());
        }
        if (user.isSupplier()) {
            supplier = supplierMapper.selectByMaterialSupplierCreatorUserId(user.getId());
        }
        return toDetailResponse(user, hospital, supplier);
    }

    public void verifyUser(Long id) {
        UserInfoEntity user = getUser(id);
        user.verify();
        updateById(user);
    }

    public UserInfoEntity getUser(Long id) {
        return Optional.ofNullable(getById(id)).orElseThrow(UserNotFoundException::new);
    }

    private void addOrganisationClaimToJwt(UserInfoEntity user, JwtBuilder jwtBuilder) {
        if (user.isHospital()) {
            HospitalInfoEntity hospital = hospitalInfoMapper.selectByHospitalCreatorUserId(user.getId());
            jwtBuilder.claim("organisationId", hospital.getId());
            jwtBuilder.claim("organisationName", hospital.getHospitalName());
            return;
        }
        if (user.isSupplier()) {
            SupplierInfoEntity supplier = supplierMapper.selectByMaterialSupplierCreatorUserId(user.getId());
            jwtBuilder.claim("organisationId", supplier.getId());
            jwtBuilder.claim("organisationName", supplier.getMaterialSupplierName());
        }
    }

    private UserResponse toResponse(UserInfoEntity user) {
        return UserResponse.builder()
                .id(user.getId().toString())
                .nickName(user.getUserNickName())
                .gmtCreated(user.getGmtCreated())
                .gmtModified(user.getGmtModified())
                .phone(user.getUserPhone())
                .role(UserRole.getRoleById(user.getUserRoleId()).name())
                .status(user.getStatus())
                .build();
    }

    private UserDetailResponse toDetailResponse(UserInfoEntity user,
                                                HospitalInfoEntity hospital,
                                                SupplierInfoEntity supplier) {
        UserDetailResponse.UserDetailResponseBuilder builder = UserDetailResponse.builder()
                .id(user.getId().toString())
                .nickName(user.getUserNickName())
                .gmtCreated(user.getGmtCreated())
                .gmtModified(user.getGmtModified())
                .phone(user.getUserPhone())
                .role(UserRole.getRoleById(user.getUserRoleId()).name())
                .status(user.getStatus());
        if (nonNull(hospital)) {
            builder.hospital(HospitalResponse.builder()
                    .id(hospital.getId().toString())
                    .name(hospital.getHospitalName())
                    .uniformSocialCreditCode(hospital.getHospitalUniformSocialCreditCode())
                    .contactorName(hospital.getHospitalContactorName())
                    .contactorTelephone(hospital.getHospitalContactorTelephone())
                    .gmtCreated(hospital.getGmtCreated())
                    .gmtModified(hospital.getGmtModified())
                    .build());
        }
        if (nonNull(supplier)) {
            builder.supplier(SupplierResponse.builder()
                    .id(supplier.getId().toString())
                    .name(supplier.getMaterialSupplierName())
                    .contactorName(supplier.getMaterialSupplierContactorName())
                    .contactorTelephone(supplier.getMaterialSupplierContactorPhone())
                    .haveLogistics(supplier.getHaveLogistics())
                    .gmtCreated(supplier.getGmtCreated())
                    .gmtModified(supplier.getGmtModified())
                    .build());
        }
        return builder.build();
    }
}
