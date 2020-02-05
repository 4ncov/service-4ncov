package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.hospital.HospitalSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.hospital.HospitalResponse;
import com.ncov.module.service.HospitalService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hospitals")
@AllArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @ApiOperation(
            value = "Hospital signup.",
            tags = SwaggerConstants.TAG_HOSPITALS
    )
    @PostMapping("/sign-up")
    @ResponseStatus(code = HttpStatus.CREATED)
    public RestResponse<HospitalResponse> signUp(@RequestBody HospitalSignUpRequest signupRequest) {
        HospitalResponse hospitalResponse = hospitalService.signUp(signupRequest);
        return RestResponse.getResp("Hospital signed up.", hospitalResponse);
    }
}
