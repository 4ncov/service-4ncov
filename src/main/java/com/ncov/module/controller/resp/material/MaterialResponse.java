package com.ncov.module.controller.resp.material;

import com.ncov.module.controller.dto.MaterialDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponse {

    @ApiModelProperty(value = "需求/供应物资唯一ID")
    private Long id;
    @ApiModelProperty(value = "物资信息")
    private MaterialDto material;
    @ApiModelProperty(value = "寻求方/供货方机构名称")
    private String organisationName;
    @ApiModelProperty(value = "收货/发货地址")
    private String address;
    @ApiModelProperty(value = "联系人")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    private String contactorPhone;
    @ApiModelProperty(value = "备注")
    private String comment;
    @ApiModelProperty(value = "状态")
    private String status;
    @ApiModelProperty(value = "图片URL列表")
    private List<String> imageUrls;
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreated;
    @ApiModelProperty(value = "上次更新时间")
    private Date gmtModified;
}
