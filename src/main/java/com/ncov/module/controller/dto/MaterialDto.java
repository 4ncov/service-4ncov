package com.ncov.module.controller.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MaterialDto {

    @ApiModelProperty(value = "物资名称")
    @NotEmpty(message = "物资名称不能为空")
    private String name;
    @ApiModelProperty(value = "物资数量")
    @NotNull(message = "物资数量不能为空")
    @Positive(message = "物资数量必须为正数")
    private Double quantity;
    @ApiModelProperty(value = "执行标准")
    @NotEmpty(message = "执行标准不能为空")
    private String standard;
    @ApiModelProperty(value = "物资类别")
    @NotEmpty(message = "物资类别不能为空")
    private String category;
}
