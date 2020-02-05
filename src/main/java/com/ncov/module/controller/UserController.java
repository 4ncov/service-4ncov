package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.user.PasswordResetRequest;
import com.ncov.module.controller.request.user.SignInRequest;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.user.SignInResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @ApiOperation(
            value = "User sign in.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PostMapping("/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public RestResponse<SignInResponse> signIn(@RequestBody SignInRequest signInRequest) {
        return RestResponse.getResp("Sign-in successful.",
                SignInResponse.builder().token("token123456").expiresAt(new Date()).build());
    }

    @ApiOperation(
            value = "Reset password.",
            tags = SwaggerConstants.TAG_USERS
    )
    @ResponseStatus(code = HttpStatus.OK)
    public RestResponse<Object> resetPassword(@RequestBody PasswordResetRequest passwordResetRequest) {
        return RestResponse.getResp("Password reset successful.");
    }
}
