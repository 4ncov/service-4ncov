package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

@Service("userInfoService")
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfoEntity> {

    public UserInfoEntity saveUserInfo(UserInfoEntity userInfoEntity) {
        this.save(userInfoEntity);
        return userInfoEntity;
    }

    public Integer findUserCountByPhoneOrNickName(String phone, String nickName) {
        QueryWrapper<UserInfoEntity> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_phone", phone).or().eq("user_nick_name", nickName);
        return count(queryWrapper);
    }
}
