package com.ncov.module.controller.request.material;

import com.ncov.module.controller.dto.MaterialDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {

    @ApiModelProperty(value = "物资信息")
    private List<MaterialDto> materials;
    @ApiModelProperty(value = "发布/供货机构名称")
    private String hospitalName;
    @ApiModelProperty(value = "收货/发货地址")
    private String address;
    @ApiModelProperty(value = "联系人")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    private String contactorPhone;
    @ApiModelProperty(value = "物资图片, 支持多张")
    private List<String> imageUrls;
    @ApiModelProperty(value = "备注")
    private String comment;
}
