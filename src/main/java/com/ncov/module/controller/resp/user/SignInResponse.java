package com.ncov.module.controller.resp.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInResponse {

    @ApiModelProperty(value = "Access token")
    private String token;
    @ApiModelProperty(value = "token过期时间")
    private Date expiresAt;
}
