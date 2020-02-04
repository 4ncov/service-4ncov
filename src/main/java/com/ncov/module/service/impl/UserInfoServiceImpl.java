package com.ncov.module.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_phone",phone).or().eq("user_nick_name",nickName);
        int count = this.count(queryWrapper);
//        Map<String,Object> parms=new HashMap<>();
//        parms.put("phone",phone);
//        parms.put("nickName",nickName);
        return count;
    }
}
