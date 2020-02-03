package com.ncov.module.controller.resp.requiredmaterial;

import com.ncov.module.controller.dto.MaterialDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialResponse {

    private Long id;
    private MaterialDto material;
    private String address;
    private String contactorName;
    private String contactorPhone;
    private String comment;
    private String status;
    private Date gmtCreated;
    private Date gmtModified;
}
