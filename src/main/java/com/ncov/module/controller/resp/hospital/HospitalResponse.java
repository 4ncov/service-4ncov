package com.ncov.module.controller.resp.hospital;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HospitalResponse {

    @ApiModelProperty(value = "需求方唯一ID")
    private Long id;
    @ApiModelProperty(value = "需求方机构名称")
    private String name;
    @ApiModelProperty(value = "18位社会统一信用代码")
    private String uniformSocialCreditCode;
    @ApiModelProperty(value = "需求方联系人")
    private String contactorName;
    @ApiModelProperty(value = "申请人身份证号")
    private String identificationNumber;
    @ApiModelProperty(value = "需求方联系电话")
    private String contactorTelephone;
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreated;
    @ApiModelProperty(value = "上次更新时间")
    private Date gmtModified;
}
