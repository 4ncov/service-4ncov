package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.Constants;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.common.exception.UserNotFoundException;
import com.ncov.module.controller.request.user.PasswordResetRequest;
import com.ncov.module.controller.resp.user.SignInResponse;
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
            throw new DuplicateException("该用户已经注册，请使用手机号登陆！");
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
        String token = jwtBuilder
                .setExpiration(jwtExpirationTime)
                .setId(UUID.randomUUID().toString())
                .setIssuer(Constants.JWT_ISSUER)
                .setIssuedAt(now)
                .claim("userRole", UserRole.getRoleById(user.getUserRoleId()))
                .claim("userNickName", user.getUserNickName())
                .claim("id", user.getId())
                .compact();
        return SignInResponse.builder().token(token).expiresAt(jwtExpirationTime).build();
    }

    public void resetPassword(PasswordResetRequest request) {
        log.info("Resetting password, phone=[{}]", request.getTelephone());
        UserInfoEntity user = Optional.ofNullable(userInfoMapper.findByUserPhone(request.getTelephone()))
                .orElseThrow(UserNotFoundException::new);
        if (!user.isAbleToResetPassword(request.getTelephone(), request.getIdentificationNumber())) {
            log.warn("Telephone and identification number not match, cannot reset password");
            throw new UserNotFoundException("Identification number not match");
        }

        user.changePassword(request.getPassword());
        updateById(user);
        log.info("Password reset, phone=[{}]", request.getTelephone());
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
}
