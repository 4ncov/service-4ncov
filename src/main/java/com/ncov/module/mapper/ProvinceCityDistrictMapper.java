package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.entity.ProvinceCityDistrictEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProvinceCityDistrictMapper extends BaseMapper<ProvinceCityDistrictEntity> {

    List<String> selectDistinctProvinces();

    List<String> selectDistinctCitiesByProvince(String province);

    List<String> selectDistinctDistrictsByProvinceAndCity(@Param("province") String province,
                                                          @Param("city") String city);
}
