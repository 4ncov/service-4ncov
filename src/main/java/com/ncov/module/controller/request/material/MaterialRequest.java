package com.ncov.module.controller.request.material;

import com.ncov.module.controller.dto.MaterialDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaterialRequest {

    private List<MaterialDto> materials;
    private String hospitalName;
    private String address;
    private String contactorName;
    private String contactorPhone;
    private List<String> imageUrls;
    private String comment;
}
