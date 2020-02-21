package com.ncov.module.controller.request.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class ContactUsCreateRequest {

    @ApiModelProperty(value = "联系电话")
    @NotEmpty(message = "联系电话不能为空")
    private String telephone;

    @ApiModelProperty(value = "联系内容")
    @NotEmpty(message = "联系内容不能为空")
    private String content;

}
