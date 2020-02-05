package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("hospitalInfoService")
@Slf4j
@AllArgsConstructor
public class HospitalService extends ServiceImpl<HospitalInfoMapper, HospitalInfoEntity> {

    private final UserInfoService userInfoService;

    @Transactional(rollbackFor = Exception.class)
    public HospitalResponse signUp(HospitalSignUpRequest hospitalSignUpRequest) {
        UserInfoEntity userInfo = saveUserInfo(hospitalSignUpRequest);
        HospitalInfoEntity hospitalInfo = saveHospitalInfo(hospitalSignUpRequest, userInfo.getId());
        return HospitalResponse.builder()
                .id(hospitalInfo.getId())
                .contactorName(hospitalInfo.getHospitalContactorName())
                .gmtCreated(hospitalInfo.getGmtCreated())
                .name(hospitalInfo.getHospitalName())
                .contactorTelephone(hospitalInfo.getHospitalContactorTelephone())
                .uniformSocialCreditCode(hospitalInfo.getHospitalUniformSocialCreditCode())
                .gmtModified(hospitalInfo.getGmtModified())
                .build();
    }

    private HospitalInfoEntity saveHospitalInfo(HospitalSignUpRequest hospitalSignUpRequest, Long userId) {
        List<String> imageUrls = hospitalSignUpRequest.getImageUrls();
        HospitalInfoEntity hospitalInfo = HospitalInfoEntity.builder()
                .hospitalContactorName(hospitalSignUpRequest.getContactorName())
                .hospitalContactorTelephone(hospitalSignUpRequest.getContactorTelephone())
                .hospitalCreatorUserId(userId)
                .hospitalName(hospitalSignUpRequest.getName())
                .hospitalUniformSocialCreditCode(hospitalSignUpRequest.getUniformSocialCreditCode())
                .gmtCreated(new Date())
                .build();
        hospitalInfo.setHospitalVerifyImageUrls(imageUrls);

        this.save(hospitalInfo);
        return hospitalInfo;
    }

    private UserInfoEntity saveUserInfo(HospitalSignUpRequest hospitalSignUpRequest) {
        return userInfoService.createUniqueUser(UserInfoEntity.builder()
                .userNickName(hospitalSignUpRequest.getName())
                .userPhone(hospitalSignUpRequest.getContactorTelephone())
                .userRoleId(UserRole.HOSPITAL.getRoleId())
                .userPasswordSHA256(DigestUtils.sha256Hex(hospitalSignUpRequest.getPassword()))
                .gmtCreated(new Date())
                .build());
    }

}
