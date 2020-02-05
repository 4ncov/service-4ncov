package com.ncov.module.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequest {

    @ApiModelProperty(value = "登录电话号码")
    private String telephone;
    @ApiModelProperty(value = "注册人身份证号")
    private String identificationNumber;
    @ApiModelProperty(value = "重置的密码")
    private String password;
}
