package com.ncov.module.controller.resp.file;

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
public class ImageUploadResponse {

    @ApiModelProperty(value = "图片URL")
    private String url;
    @ApiModelProperty(value = "创建时间")
    private Date gmtCreated;
}
