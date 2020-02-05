package com.ncov.module.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.SupplierMapper;
import com.ncov.module.entity.SupplierInfoEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("supplierService")
@Slf4j
@AllArgsConstructor
public class SupplierService extends ServiceImpl<SupplierMapper, SupplierInfoEntity> {

    private final UserInfoService userInfoService;

    @Transactional(rollbackFor = Exception.class)
    public SupplierResponse signUp(SupplierSignUpRequest supplierSignUpRequest) {
        UserInfoEntity userInfo = this.saveUserInfo(supplierSignUpRequest);
        SupplierInfoEntity supplierInfo = this.saveSupplierInfo(supplierSignUpRequest, userInfo.getId());
        return SupplierResponse.builder()
                .id(supplierInfo.getId())
                .address(supplierInfo.getMaterialSupplierCompanyAddress())
                .contactorName(supplierInfo.getMaterialSupplierContactorName())
                .gmtCreated(supplierInfo.getGmtCreated())
                .haveLogistics(supplierInfo.getHaveLogistics())
                .name(supplierInfo.getMaterialSupplierName())
                .contactorTelephone(supplierInfo.getMaterialSupplierContactorPhone())
                .uniformSocialCreditCode(supplierInfo.getMaterialSupplierUniformSocialCreditCode())
                .gmtModified(supplierInfo.getGmtModified())
                .build();
    }

    private SupplierInfoEntity saveSupplierInfo(SupplierSignUpRequest supplierSignUpRequest, Long userId) {
        List<String> imageUrls = supplierSignUpRequest.getImageUrls();
        SupplierInfoEntity supplierInfo = SupplierInfoEntity.builder()
                .materialSupplierName(supplierSignUpRequest.getName())
                .materialSupplierContactorName(supplierSignUpRequest.getContactorName())
                .materialSupplierContactorPhone(supplierSignUpRequest.getContactorTelephone())
                .materialSupplierCompanyAddress(supplierSignUpRequest.getAddress())
                .materialSupplierUniformSocialCreditCode(supplierSignUpRequest.getUniformSocialCreditCode())
                .materialSupplierCreatorUserId(userId)
                .gmtCreated(new Date())
                .build();
        supplierInfo.setMaterialSupplierVerifyImageUrls(imageUrls);

        this.save(supplierInfo);
        return supplierInfo;
    }

    private UserInfoEntity saveUserInfo(SupplierSignUpRequest supplierSignUpRequest) {
        Integer count = userInfoService.findUserCountByPhoneOrNickName(
                supplierSignUpRequest.getContactorTelephone(), supplierSignUpRequest.getContactorName());
        if (count > 0) {
            throw new DuplicateException("该供应商已经注册，请使用手机号登陆！！");
        }
        return this.userInfoService.saveUserInfo(UserInfoEntity.builder()
                .userNickName(supplierSignUpRequest.getName())
                .gmtCreated(new Date())
                .userPhone(supplierSignUpRequest.getContactorTelephone())
                .userRoleId(UserRole.SUPPLIER.getRoleId())
                .build());
    }
}
