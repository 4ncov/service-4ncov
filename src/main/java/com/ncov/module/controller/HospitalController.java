package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.service.HospitalInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hospitals")
@AllArgsConstructor
public class HospitalController {

    private final HospitalInfoService hospitalInfoService;

    @ApiOperation(
            value = "Hospital signup.",
            tags = SwaggerConstants.TAG_HOSPITALS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse<HospitalResponse>> signUp(@RequestBody HospitalSignUpRequest signupRequest) {
        HospitalResponse hospitalResponse = hospitalInfoService.signUp(signupRequest);
        return new ResponseEntity<>(RestResponse.<HospitalResponse>builder()
                .message("Hospital signed up.")
                .data(hospitalResponse)
                .build(), HttpStatus.OK);
    }
}
