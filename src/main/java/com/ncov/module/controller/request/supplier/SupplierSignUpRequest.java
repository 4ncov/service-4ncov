package com.ncov.module.controller.request.supplier;

import com.ncov.module.controller.dto.SupplierDto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupplierSignUpRequest {

    private SupplierDto supplier;
    private Boolean isDeliveryAvailable;
    private String certificateImageUrl;
}
