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
    @ApiModelProperty(value = "发布/供货机构名称", required = true)
    private String organisationName;
    @ApiModelProperty(value = "收货/发货地址", required = true)
    private String address;
    @ApiModelProperty(value = "联系人", required = true)
    private String contactorName;
    @ApiModelProperty(value = "联系电话", required = true)
    private String contactorPhone;
    @ApiModelProperty(value = "物资图片, 支持多张")
    private List<String> imageUrls;
    @ApiModelProperty(value = "备注")
    private String comment;
    @ApiModelProperty(value = "第几页", notes = "只有分页查询才需要传入数据", required = false)
    private Integer pageSize;
    @ApiModelProperty(value = "每页行数", notes = "只有分页查询才需要传入数据", required = false)
    private Integer pageNums;
}
