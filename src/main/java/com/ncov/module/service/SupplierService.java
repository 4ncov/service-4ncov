package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.entity.SupplierInfoEntity;

public interface SupplierService extends IService<SupplierInfoEntity> {
    /**
     * 供应商注册接口
     * @param supplierSignUpRequest
     * @return
     */
    SupplierResponse signUp(SupplierSignUpRequest supplierSignUpRequest);
}
