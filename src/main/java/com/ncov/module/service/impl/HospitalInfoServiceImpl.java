package com.ncov.module.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.exception.RRException;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.HospitalInfoMapper;
import com.ncov.module.service.HospitalInfoService;
import com.ncov.module.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service("HospitalInfoService")
public class HospitalInfoServiceImpl extends ServiceImpl<HospitalInfoMapper, HospitalInfoEntity> implements HospitalInfoService {

    private static Logger logger = LoggerFactory.getLogger(SupplierServiceImpl.class);
    //医院角色id
    private final Long HOSPITAL_ROLE_ID= Long.valueOf(3);

    @Autowired
    private UserInfoService userInfoService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public HospitalResponse signUp(HospitalSignUpRequest hospitalSignUpRequest) {
        //用户id
        Long userId=this.saveUserInfo(hospitalSignUpRequest);
        HospitalInfoEntity hospitalInfoEntity = this.saveHospitalInfo(hospitalSignUpRequest,userId);
        HospitalResponse hospitalResponse=new HospitalResponse();
        hospitalResponse.setId(hospitalInfoEntity.getId());
        hospitalResponse.setAddress(hospitalInfoEntity.getHospitalDetailAddress());
        hospitalResponse.setContactorName(hospitalInfoEntity.getHospitalContactorName());
        hospitalResponse.setGmtCreated(hospitalInfoEntity.getGmtCreated());
        hospitalResponse.setName(hospitalInfoEntity.getHospitalName());
        hospitalResponse.setContactorTelephone(hospitalInfoEntity.getHospitalContactorTelephone());
        hospitalResponse.setUniformSocialCreditCode(hospitalInfoEntity.getHospitalUniformSocialCreditCode());
        hospitalResponse.setGmtModified(hospitalInfoEntity.getGmtModified());
        hospitalResponse.setAddressCode(hospitalInfoEntity.getHospitalAddressCode());
        return hospitalResponse;
    }

    /**
     * 保存供应商信息
     */
    public HospitalInfoEntity saveHospitalInfo(HospitalSignUpRequest hospitalSignUpRequest, Long userId){
        HospitalInfoEntity hospitalInfoEntity=new HospitalInfoEntity();
        List<String> imageUrls=hospitalSignUpRequest.getImageUrls();
        hospitalInfoEntity.setHospitalContactorName(hospitalSignUpRequest.getContactorName());
        hospitalInfoEntity.setHospitalContactorTelephone(hospitalSignUpRequest.getContactorTelephone());
        hospitalInfoEntity.setHospitalCreatorUserId(userId);
        hospitalInfoEntity.setHospitalDetailAddress(hospitalSignUpRequest.getAddress());
        hospitalInfoEntity.setHospitalName(hospitalSignUpRequest.getName());
        hospitalInfoEntity.setHospitalUniformSocialCreditCode(hospitalSignUpRequest.getUniformSocialCreditCode());
        hospitalInfoEntity.setHospitalAddressCode(hospitalSignUpRequest.getAddressCode());
        //判断是否有附件
        if(imageUrls!=null){
            int count=imageUrls.size();
            for(int i=0;i<3;i++){
                if(count<=i){
                    break;
                }
                if(i==0){
                    hospitalInfoEntity.setHospitalVerifyInfoUrl1(imageUrls.get(i));
                }
                else if(i==1){
                    hospitalInfoEntity.setHospitalVerifyInfoUrl2(imageUrls.get(i));
                }
                else if(i==2){
                    hospitalInfoEntity.setHospitalVerifyInfoUrl3(imageUrls.get(i));
                }
            }
        }
        hospitalInfoEntity.setGmtCreated(new Date());
        this.save(hospitalInfoEntity);
        return hospitalInfoEntity;
    }

    /**
     * 保存用户信息，返回用户id
     * @param hospitalSignUpRequest
     * @return
     */
    public Long  saveUserInfo(HospitalSignUpRequest hospitalSignUpRequest){
//        Integer count=userInfoService.findUserCountByPhoneOrNickName(hospitalSignUpRequest.getContactorTelephone(),hospitalSignUpRequest.getContactorName());
//        if(count>0){
//            throw new RRException("该供应商已经注册，请使用手机号登陆！！");
//        }
        UserInfoEntity userInfoEntity=new UserInfoEntity();
        //昵称为医院名称
        userInfoEntity.setUserNickName(hospitalSignUpRequest.getName());
        userInfoEntity.setGmtCreated(new Date());
        //手机号为联系人手机号
        userInfoEntity.setUserPhone(hospitalSignUpRequest.getContactorTelephone());
        userInfoEntity.setUserRoleId(HOSPITAL_ROLE_ID);
        this.userInfoService.saveUserInfo(userInfoEntity);
        return userInfoEntity.getId();
    }

}
