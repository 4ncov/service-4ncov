package com.ncov.module.controller.request.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class SupplierSignUpRequest {
    @ApiModelProperty(value = "供应商公司名称")
    @NotEmpty(message = "供应商公司名称不能为空")
    private String name;
    @ApiModelProperty(value = "供应商图片上传, 支持多张图片")
    private List<String> imageUrls;
    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "联系人姓名不能为空")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "联系电话不能为空")
    private String contactorTelephone;
    @ApiModelProperty(value = "登录密码")
    @NotEmpty(message = "登录密码不能为空")
    private String password;
    @ApiModelProperty(value = "是否自备物流")
    @NotNull(message = "必须选择是否自备物流")
    private Boolean haveLogistics;
}
