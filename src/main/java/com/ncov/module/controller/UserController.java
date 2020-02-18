package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.user.SignInRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.user.SignInResponse;
import com.ncov.module.controller.resp.user.UserDetailResponse;
import com.ncov.module.controller.resp.user.UserResponse;
import com.ncov.module.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private final UserInfoService userInfoService;

    @ApiOperation(
            value = "User sign in.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PostMapping("/sign-in")
    @ResponseStatus(code = HttpStatus.OK)
    public RestResponse<SignInResponse> signIn(@RequestBody @Valid SignInRequest signInRequest) {
        SignInResponse response = userInfoService.signIn(signInRequest.getTelephone(), signInRequest.getPassword());
        return RestResponse.getResp("Sign-in successful.", response);
    }

    @ApiOperation(
            value = "Admin verify user.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PatchMapping("/{id}:verify")
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse verify(@PathVariable Long id) {
        userInfoService.verifyUser(id);
        return RestResponse.getResp("认证成功.");
    }

    @ApiOperation(
            value = "Admin list all users.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<UserResponse> listAllUsers(@RequestParam Integer page, @RequestParam Integer size) {
        return userInfoService.listAllUsers(page, size);
    }

    @ApiOperation(
            value = "Admin get user detail.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse<UserDetailResponse> getUser(@PathVariable Long id) {
        return RestResponse.getResp("User fetched.", userInfoService.getDetail(id));
    }
}
