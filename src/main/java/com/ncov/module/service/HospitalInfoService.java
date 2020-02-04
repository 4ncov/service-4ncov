package com.ncov.module.service;

import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.hospital.HospitalResponse;

public interface HospitalInfoService {
    /**
     * 医院注册接口
     * @param hospitalSignUpRequest
     * @return
     */
    HospitalResponse signUp(HospitalSignUpRequest hospitalSignUpRequest);
}
