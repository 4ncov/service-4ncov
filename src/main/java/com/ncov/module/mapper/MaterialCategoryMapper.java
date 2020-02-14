package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.MaterialCategoryEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface MaterialCategoryMapper extends BaseMapper<MaterialCategoryEntity> {

    List<MaterialCategoryEntity> selectAll();
}
