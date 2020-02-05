package com.ncov.module.controller.resp.supplier;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierResponse {

    @ApiModelProperty(value = "供应商唯一ID")
    private Long id;
    @ApiModelProperty(value = "供应商公司名称")
    private String name;
    @ApiModelProperty(value = "申请人身份证号")
    private String identificationNumber;
    @ApiModelProperty(value = "联系人")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    private String contactorTelephone;
    @ApiModelProperty(value = "是否自备物流")
    private Boolean haveLogistics;
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreated;
    @ApiModelProperty(value = "上次更新时间")
    private Date gmtModified;
}
