package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.ContactUsEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface ContactUsMapper extends BaseMapper<ContactUsEntity> {
}
