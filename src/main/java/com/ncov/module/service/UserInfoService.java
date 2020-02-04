package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncov.module.entity.UserInfoEntity;

public interface UserInfoService extends IService<UserInfoEntity> {
    /**
     * 保存用户信息
     * @param userInfoEntity
     * @return
     */
    UserInfoEntity saveUserInfo(UserInfoEntity userInfoEntity);

    /**
     * 按照手机号或者昵称查询
     * @param phone
     * @param nickName
     * @return
     */
    Integer findUserCountByPhoneOrNickName(String phone,String nickName);
}
