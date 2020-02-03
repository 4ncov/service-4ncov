package com.ncov.module.controller.request.suppliedmaterial;

import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.dto.SupplierDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplementRequest {

    private List<MaterialDto> commodities;
    private SupplierDto supplier;
    private String supplementImageUrl;
    private String comment;
}
