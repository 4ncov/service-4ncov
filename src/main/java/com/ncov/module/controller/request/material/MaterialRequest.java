package com.ncov.module.controller.request.material;

import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class MaterialRequest {

    @ApiModelProperty(value = "物资信息")
    @NotEmpty(message = "需要至少一种物资信息")
    @Valid
    private List<MaterialDto> materials;
    @ApiModelProperty(value = "发布/供货机构名称")
    @NotEmpty(message = "机构名称不能为空")
    private String organisationName;
    @ApiModelProperty(value = "发布/供货机构Logo")
    private String organisationLogo;
    @ApiModelProperty(value = "收货/发货地址")
    @Valid
    private AddressDto address;
    @ApiModelProperty(value = "联系人")
    @NotEmpty(message = "联系人姓名不能为空")
    private String contactorName;
    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "联系电话不能为空")
    private String contactorPhone;
    @ApiModelProperty(value = "备注")
    private String comment;
}
