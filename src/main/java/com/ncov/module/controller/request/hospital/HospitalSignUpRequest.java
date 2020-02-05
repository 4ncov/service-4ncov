package com.ncov.module.controller.request.hospital;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class HospitalSignUpRequest {

    @ApiModelProperty(value = "需求方机构名称")
    @NotEmpty(message = "机构名称不能为空")
    private String name;
    @ApiModelProperty(value = "18位社会统一信用代码")
    @NotEmpty(message = "社会统一信用代码不能为空")
    @Size(min = 18, max = 18, message = "社会统一信用代码必须为18位")
    private String uniformSocialCreditCode;
    @ApiModelProperty(value = "上传的图片地址, 支持多个图片")
    @NotEmpty(message = "必须上传至少一张申请人身份证明图片")
    private List<String> imageUrls;
    @ApiModelProperty(value = "申请人身份证号")
    @NotEmpty(message = "申请人身份证号不能为空")
    private String identificationNumber;
    @ApiModelProperty(value = "需求方联系人")
    @NotEmpty(message = "联系人姓名不能为空")
    private String contactorName;
    @ApiModelProperty(value = "需求方联系电话")
    @NotEmpty(message = "联系人电话不能为空")
    private String contactorTelephone;
    @ApiModelProperty(value = "需求方登录密码")
    @NotEmpty(message = "登录密码不能为空")
    private String password;
}
