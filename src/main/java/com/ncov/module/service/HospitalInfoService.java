package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("hospitalInfoService")
@Slf4j
@AllArgsConstructor
public class HospitalInfoService extends ServiceImpl<HospitalInfoMapper, HospitalInfoEntity> {

    private final UserInfoService userInfoService;

    @Transactional(rollbackFor = Exception.class)
    public HospitalResponse signUp(HospitalSignUpRequest hospitalSignUpRequest) {
        UserInfoEntity userInfo = saveUserInfo(hospitalSignUpRequest);
        HospitalInfoEntity hospitalInfo = saveHospitalInfo(hospitalSignUpRequest, userInfo.getId());
        return HospitalResponse.builder()
                .id(hospitalInfo.getId())
                .address(hospitalInfo.getHospitalDetailAddress())
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
                .hospitalDetailAddress(hospitalSignUpRequest.getAddress())
                .hospitalName(hospitalSignUpRequest.getName())
                .hospitalUniformSocialCreditCode(hospitalSignUpRequest.getUniformSocialCreditCode())
                .gmtCreated(new Date())
                .build();
        hospitalInfo.setHospitalVerifyImageUrls(imageUrls);

        this.save(hospitalInfo);
        return hospitalInfo;
    }

    private UserInfoEntity saveUserInfo(HospitalSignUpRequest hospitalSignUpRequest) {
        Integer count = userInfoService.findUserCountByPhoneOrNickName(
                hospitalSignUpRequest.getContactorTelephone(), hospitalSignUpRequest.getContactorName());
        if (count > 0) {
            throw new DuplicateException("该医院已经注册，请使用手机号登陆！！");
        }
        return this.userInfoService.saveUserInfo(UserInfoEntity.builder()
                .userNickName(hospitalSignUpRequest.getName())
                .userPhone(hospitalSignUpRequest.getContactorTelephone())
                .userRoleId(UserRole.HOSPITAL.getRoleId())
                .gmtCreated(new Date())
                .build());
    }

}
