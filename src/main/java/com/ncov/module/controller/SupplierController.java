package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.supplier.SupplierSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.supplier.SupplierResponse;
import com.ncov.module.service.SupplierService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suppliers")
@AllArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @ApiOperation(
            value = "Supplier sign up.",
            tags = SwaggerConstants.TAG_SUPPLIERS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse<SupplierResponse>> signUp(@RequestBody SupplierSignUpRequest signUpRequest) {
        SupplierResponse supplierResponse = supplierService.signUp(signUpRequest);
        return new ResponseEntity<>(RestResponse.<SupplierResponse>builder()
                .message("Supplier signed up.")
                .data(supplierResponse)
                .build(), HttpStatus.OK);
    }
}
