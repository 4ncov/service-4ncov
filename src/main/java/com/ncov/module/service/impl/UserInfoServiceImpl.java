package com.ncov.module.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.UserInfoMapper;
import com.ncov.module.service.UserInfoService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("userInfoService")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfoEntity> implements UserInfoService {

    @Override
    public UserInfoEntity saveUserInfo(UserInfoEntity userInfoEntity) {
        this.save(userInfoEntity);
        return userInfoEntity;
    }

    @Override
    public Integer findUserCountByPhoneOrNickName(String phone, String nickName) {
        Map<String,Object> parms=new HashMap<>();
        parms.put("phone",phone);
        parms.put("nickName",nickName);
        return this.baseMapper.selectUserCountByPhoneOrNickName(parms);
    }
}
