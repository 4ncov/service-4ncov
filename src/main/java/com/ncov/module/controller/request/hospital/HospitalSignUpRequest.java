package com.ncov.module.controller.request.hospital;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalSignUpRequest {

    @ApiModelProperty(value = "需求方机构名称")
    private String name;
    @ApiModelProperty(value = "18位社会统一信用代码")
    private String uniformSocialCreditCode;
    @ApiModelProperty(value = "上传的图片地址, 支持多个图片")
    private List<String> imageUrls;
    @ApiModelProperty(value = "需求方联系人")
    private String contactorName;
    @ApiModelProperty(value = "需求方联系电话")
    private String contactorTelephone;
    @ApiModelProperty(value = "需求方登录密码")
    private String password;
}
