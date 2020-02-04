package com.ncov.module.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.exception.RRException;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.SupplierMapper;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.service.SupplierService;
import com.ncov.module.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("supplierService")
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, SupplierInfoEntity> implements SupplierService {

    private static Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);
    //供应商角色id
    private final Long SUPPLIER_ROLE_ID= Long.valueOf(2);

    @Autowired
    private UserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SupplierResponse signUp(SupplierSignUpRequest supplierSignUpRequest) {
        //用户id
        Long userId=this.saveUserInfo(supplierSignUpRequest);
        SupplierInfoEntity supplierInfoEntity = this.saveSupplierInfo(supplierSignUpRequest,userId);
        SupplierResponse supplierResponse=new SupplierResponse();
        supplierResponse.setId(supplierInfoEntity.getId());
        supplierResponse.setAddress(supplierInfoEntity.getMaterialSupplierCompanyAddress());
        supplierResponse.setContactorName(supplierInfoEntity.getMaterialSupplierContactorName());
        supplierResponse.setGmtCreated(supplierInfoEntity.getGmtCreated());
        supplierResponse.setHaveLogistics(supplierSignUpRequest.getHaveLogistics());
        supplierResponse.setName(supplierInfoEntity.getMaterialSupplierName());
        supplierResponse.setContactorTelephone(supplierInfoEntity.getMaterialSupplierContactorPhone());
        supplierResponse.setUniformSocialCreditCode(supplierInfoEntity.getMaterialSupplierUniformSocialCreditCode());
        supplierResponse.setGmtModified(supplierInfoEntity.getGmtModified());
        return supplierResponse;
    }

    /**
     * 保存供应商信息
     */
    public SupplierInfoEntity saveSupplierInfo(SupplierSignUpRequest supplierSignUpRequest,Long userId){
        SupplierInfoEntity supplierInfoEntity=new SupplierInfoEntity();
        List<String> imageUrls=supplierSignUpRequest.getImageUrls();
        supplierInfoEntity.setMaterialSupplierName(supplierSignUpRequest.getName());
        supplierInfoEntity.setMaterialSupplierContactorName(supplierSignUpRequest.getContactorName());
        supplierInfoEntity.setMaterialSupplierContactorPhone(supplierSignUpRequest.getContactorTelephone());
        supplierInfoEntity.setMaterialSupplierCompanyAddress(supplierSignUpRequest.getAddress());
        supplierInfoEntity.setMaterialSupplierUniformSocialCreditCode(supplierSignUpRequest.getUniformSocialCreditCode());
        //判断是否有附件
        if(imageUrls!=null){
            int count=imageUrls.size();
            for(int i=0;i<3;i++){
                if(count<=i){
                    break;
                }
                if(i==0){
                    supplierInfoEntity.setMaterialSupplierVerifyInfoUrl1(imageUrls.get(i));
                }
                else if(i==1){
                    supplierInfoEntity.setMaterialSupplierVerifyInfoUrl2(imageUrls.get(i));
                }
                else if(i==2){
                    supplierInfoEntity.setMaterialSupplierVerifyInfoUrl3(imageUrls.get(i));
                }
            }
        }
        supplierInfoEntity.setMaterialSupplierCreatorUserId(userId);
        //判断是否有物流字段是否为空
        if(supplierSignUpRequest.getHaveLogistics()!=null){
            //有物流数据库保存1，没有物流数据库保存0
            if(supplierSignUpRequest.getHaveLogistics()){
                supplierInfoEntity.setHaveLogistics(1);
            }else{
                supplierInfoEntity.setHaveLogistics(0);
            }
        }else{
            supplierInfoEntity.setHaveLogistics(0);
        }
        supplierInfoEntity.setGmtCreated(new Date());
        this.save(supplierInfoEntity);
        return supplierInfoEntity;
    }

    /**
     * 保存用户信息，返回用户id
     * @param supplierSignUpRequest
     * @return
     */
    public Long  saveUserInfo(SupplierSignUpRequest supplierSignUpRequest){
        Integer count=userInfoService.findUserCountByPhoneOrNickName(supplierSignUpRequest.getContactorTelephone(),supplierSignUpRequest.getContactorName());
        if(count>0){
            throw new RRException("该供应商已经注册，请使用手机号登陆！！");
        }
        UserInfoEntity userInfoEntity=new UserInfoEntity();
        //昵称为供应商名称
        userInfoEntity.setUserNickName(supplierSignUpRequest.getName());
        userInfoEntity.setGmtCreated(new Date());
        //手机号为联系人手机号
        userInfoEntity.setUserPhone(supplierSignUpRequest.getContactorTelephone());
        userInfoEntity.setUserRoleId(SUPPLIER_ROLE_ID);
        this.userInfoService.saveUserInfo(userInfoEntity);
        return userInfoEntity.getId();
    }
}
