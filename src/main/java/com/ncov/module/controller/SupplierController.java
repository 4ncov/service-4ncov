package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
public class SupplierController {

    @ApiOperation(
            value = "Supplier sign up.",
            tags = SwaggerConstants.TAG_SUPPLIERS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse> signUp(@RequestBody SupplierSignUpRequest signUpRequest) {
        // TODO: 2020-01-29
        return null;
    }
}
