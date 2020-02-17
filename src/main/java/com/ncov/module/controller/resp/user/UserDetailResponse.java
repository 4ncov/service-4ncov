package com.ncov.module.controller.resp.user;

import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private String id;
    private String nickName;
    private Date gmtCreated;
    private Date gmtModified;
    private String phone;
    private String role;
    private String status;
    private HospitalResponse hospital;
    private SupplierResponse supplier;
}
