package com.ncov.module.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialDto {

    @ApiModelProperty(value = "物资名称")
    private String name;
    @ApiModelProperty(value = "物资数量")
    private Double quantity;
    @ApiModelProperty(value = "执行标准")
    private String standard;
    @ApiModelProperty(value = "物资类别")
    private String category;
}
