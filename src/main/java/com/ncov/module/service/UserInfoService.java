package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.exception.DuplicateException;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.UserInfoMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("userInfoService")
@Slf4j
@AllArgsConstructor
public class UserInfoService extends ServiceImpl<UserInfoMapper, UserInfoEntity> {

    private final UserInfoMapper userInfoMapper;

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
}
