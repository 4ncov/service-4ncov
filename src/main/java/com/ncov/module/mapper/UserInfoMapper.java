package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {

    Integer selectCountByPhoneOrNickName(@Param("userPhone") String userPhone,
                                         @Param("userNickName") String userNickName);

    UserInfoEntity findByUserPhone(String userPhone);
}
