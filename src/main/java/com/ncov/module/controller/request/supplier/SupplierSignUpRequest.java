package com.ncov.module.controller.request.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierSignUpRequest {
    @ApiModelProperty(value = "供应商公司名称")
    private String name;
    @ApiModelProperty(value = "申请人身份证号")
    private String identificationNumber;
    @ApiModelProperty(value = "供应商图片上传, 支持多张图片")
    private List<String> imageUrls;
    @ApiModelProperty(value = "联系人")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    private String contactorTelephone;
    @ApiModelProperty(value = "登录密码")
    private String password;
    @ApiModelProperty(value = "是否自备物流")
    private Boolean haveLogistics;
}
