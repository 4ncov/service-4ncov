package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @ApiOperation(
            value = "Supplier sign up.",
            tags = SwaggerConstants.TAG_SUPPLIERS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse<SupplierResponse>> signUp(@RequestBody SupplierSignUpRequest signUpRequest) {
        // TODO: 2020-01-29
        return new ResponseEntity<>(RestResponse.<SupplierResponse>builder()
                .message("Supplier signed up.")
                .data(SupplierResponse.builder()
                        .name("武汉市一二三四有限公司")
                        .uniformSocialCreditCode("098765432109876543")
                        .address("湖北省武汉市东西湖区银潭路1号")
                        .contactorName("张三")
                        .contactorTelephone("18801234567")
                        .haveLogistics(true)
                        .gmtCreated(new Date())
                        .gmtModified(new Date())
                        .build())
                .build(), HttpStatus.OK);
    }
}
