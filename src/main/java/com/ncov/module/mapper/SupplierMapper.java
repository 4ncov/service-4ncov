package com.ncov.module.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.entity.SupplierInfoEntity;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SupplierMapper extends BaseMapper<SupplierInfoEntity> {
    /**
     * 供应商新增接口
     * @param supplierSignUpRequest
     * @return
     */
    void insterSupplierInfo(SupplierSignUpRequest supplierSignUpRequest);
}
