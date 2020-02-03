package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/hospitals")
public class HospitalController {

    @ApiOperation(
            value = "Hospital signup.",
            tags = SwaggerConstants.TAG_HOSPITALS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse<HospitalResponse>> signUp(@RequestBody HospitalSignUpRequest signupRequest) {
        // TODO
        return new ResponseEntity<>(RestResponse.<HospitalResponse>builder()
                .message("Hospital signed up.")
                .data(HospitalResponse.builder()
                        .id(3L)
                        .name("武汉市金银潭医院")
                        .uniformSocialCreditCode("123456789012345678")
                        .address("湖北省武汉市东西湖区银潭路1号")
                        .contactorName("张三")
                        .contactorTelephone("18801234567")
                        .gmtCreated(new Date())
                        .gmtModified(new Date())
                        .build())
                .build(), HttpStatus.OK);
    }
}
