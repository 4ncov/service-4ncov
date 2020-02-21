package com.ncov.module.controller.resp.category;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialCategoryResponse {

    @ApiModelProperty(name = "类型唯一ID")
    private String id;
    @ApiModelProperty(name = "类型名称")
    private String name;
    @ApiModelProperty(name = "类型单位")
    private String unit;
}
