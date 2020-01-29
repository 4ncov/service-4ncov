package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.requester.RequesterSignUpRequest;
import com.ncov.module.controller.resp.RestResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/requesters")
public class RequesterController {

    @ApiOperation(
            value = "Requester signup.",
            tags = SwaggerConstants.TAG_REQUESTERS
    )
    @PostMapping("/sign-up")
    public ResponseEntity<RestResponse> signUp(@RequestBody RequesterSignUpRequest signupRequest) {
        // TODO
        return null;
    }
}
