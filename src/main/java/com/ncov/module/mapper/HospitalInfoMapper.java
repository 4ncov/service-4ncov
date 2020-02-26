package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.HospitalInfoEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface HospitalInfoMapper extends BaseMapper<HospitalInfoEntity> {

    HospitalInfoEntity selectByHospitalCreatorUserId(Long hospitalCreatorUserId);
}
