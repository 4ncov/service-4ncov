package com.ncov.module.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class PasswordResetRequest {

    @ApiModelProperty(value = "登录电话号码")
    @NotEmpty(message = "登录电话不能为空")
    private String telephone;
    @ApiModelProperty(value = "注册人身份证号")
    @NotEmpty(message = "身份证号不能为空")
    @Size(min = 18, max = 18, message = "身份证号必须为18位")
    private String identificationNumber;
    @ApiModelProperty(value = "重置的密码")
    @NotEmpty(message = "新密码不能为空")
    private String password;
}
