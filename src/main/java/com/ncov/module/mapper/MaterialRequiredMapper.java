package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.MaterialRequiredEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * 物料寻求仓储接口
 * @author lucas
 */
@Repository
@Mapper
public interface MaterialRequiredMapper extends BaseMapper<MaterialRequiredEntity> {

}
