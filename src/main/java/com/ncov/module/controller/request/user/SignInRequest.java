package com.ncov.module.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Validated
public class SignInRequest {

    @ApiModelProperty(value = "登录电话号码")
    @NotEmpty(message = "登录电话号码不能为空")
    private String telephone;
    @ApiModelProperty(value = "登录密码")
    @NotEmpty(message = "登录密码不能为空")
    private String password;
}
