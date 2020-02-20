package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.user.ContactUsCreateRequest;
import com.ncov.module.controller.request.user.SignInRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.PageResponse;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.user.ContactUsResponse;
import com.ncov.module.controller.resp.user.SignInResponse;
import com.ncov.module.controller.resp.user.UserDetailResponse;
import com.ncov.module.controller.resp.user.UserResponse;
import com.ncov.module.entity.ContactUsEntity;
import com.ncov.module.service.UserInfoService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

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
    public Page<UserResponse> listAllUsers(@RequestParam Integer page, @RequestParam Integer size,
                                           @RequestParam(name = "telephone", required = false) String telephone,
                                           @RequestParam(name = "role", required = false) String role,
                                           @RequestParam(name = "status", required = false) String status) {
        return userInfoService.listAllUsers(page, size, telephone, role, status);
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

    @ApiOperation(
            value = "create contact us.",
            tags = SwaggerConstants.TAG_USERS
    )
    @PostMapping("/contact-us")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse createContactUs(@RequestBody @Valid ContactUsCreateRequest contactUsCreateRequest) {
        Date now = new Date();
        userInfoService.insert(ContactUsEntity.builder()
                .userPhone(contactUsCreateRequest.getPhone())
                .content(contactUsCreateRequest.getContent())
                .gmtCreated(now)
                .gmtModified(now)
                .build());
        return RestResponse.getResp("创建成功.");
    }

    @ApiOperation(
            value = "list contact us.",
            tags = SwaggerConstants.TAG_USERS
    )
    @GetMapping("/contact-us")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    public PageResponse<ContactUsResponse> listContactUs(@RequestParam Integer page, @RequestParam Integer size) {
        Page<ContactUsResponse> responsePage = userInfoService.selectPage(ContactUsEntity.class
                , page
                , size
                , null
                , entity -> new ContactUsResponse(entity.getId()
                        , entity.getUserPhone()
                        , entity.getContent()
                        , entity.getGmtCreated()
                        , entity.getGmtModified()));
        return PageResponse.getResp("请求成功.", responsePage);
    }

}
