package com.ncov.module.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInRequest {

    @ApiModelProperty(value = "登录电话号码")
    private String telephone;
    @ApiModelProperty(value = "登录密码")
    private String password;
}
