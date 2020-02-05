package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.SupplierMapper;
import com.ncov.module.entity.SupplierInfoEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
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
                .contactorName(supplierInfo.getMaterialSupplierContactorName())
                .gmtCreated(supplierInfo.getGmtCreated())
                .haveLogistics(supplierInfo.getHaveLogistics())
                .name(supplierInfo.getMaterialSupplierName())
                .contactorTelephone(supplierInfo.getMaterialSupplierContactorPhone())
                .identificationNumber(userInfo.getUserIdentificationNumber())
                .gmtModified(supplierInfo.getGmtModified())
                .build();
    }

    private SupplierInfoEntity saveSupplierInfo(SupplierSignUpRequest supplierSignUpRequest, Long userId) {
        List<String> imageUrls = supplierSignUpRequest.getImageUrls();
        SupplierInfoEntity supplierInfo = SupplierInfoEntity.builder()
                .materialSupplierName(supplierSignUpRequest.getName())
                .materialSupplierContactorName(supplierSignUpRequest.getContactorName())
                .materialSupplierContactorPhone(supplierSignUpRequest.getContactorTelephone())
                .materialSupplierCreatorUserId(userId)
                .haveLogistics(supplierSignUpRequest.getHaveLogistics())
                .gmtCreated(new Date())
                .build();
        supplierInfo.setMaterialSupplierVerifyImageUrls(imageUrls);

        this.save(supplierInfo);
        return supplierInfo;
    }

    private UserInfoEntity saveUserInfo(SupplierSignUpRequest supplierSignUpRequest) {
        return userInfoService.createUniqueUser(UserInfoEntity.builder()
                .userNickName(supplierSignUpRequest.getName())
                .gmtCreated(new Date())
                .userPhone(supplierSignUpRequest.getContactorTelephone())
                .userPasswordSHA256(DigestUtils.sha256Hex(supplierSignUpRequest.getPassword()))
                .userRoleId(UserRole.SUPPLIER.getRoleId())
                .userIdentificationNumber(supplierSignUpRequest.getIdentificationNumber())
                .build());
    }
}
