package com.ncov.module.controller.request.material;

import com.ncov.module.controller.dto.MaterialDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {

    private MaterialDto material;
    private String hospitalName;
    private String address;
    private String contactorName;
    private String contactorPhone;
    private String comment;
}
