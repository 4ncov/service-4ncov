package com.ncov.module.controller.resp.material;

import com.ncov.module.controller.dto.MaterialDto;
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

    private Long id;
    private List<MaterialDto> materials;
    private String address;
    private String contactorName;
    private String contactorPhone;
    private String comment;
    private String status;
    private Date gmtCreated;
    private Date gmtModified;
}
