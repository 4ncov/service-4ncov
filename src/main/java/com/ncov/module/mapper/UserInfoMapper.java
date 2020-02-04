package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.UserInfoEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfoEntity> {
    /**
     * 按照手机号或者昵称查询
     * @return
     */
    Integer selectUserCountByPhoneOrNickName( Map<String,Object> params);
}
