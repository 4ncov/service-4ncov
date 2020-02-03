package com.ncov.module.controller.resp.supplement;

import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.dto.SupplierDto;
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
public class SupplementResponse {

    private Long id;
    private List<MaterialDto> commodities;
    private SupplierDto supplier;
    private String supplementImageUrl;
    private String comment;
    private String status;
    private Date createdTime;
    private Date updatedTime;
}
