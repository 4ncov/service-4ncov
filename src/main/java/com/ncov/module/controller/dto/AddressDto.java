package com.ncov.module.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
public class AddressDto {

    private String country;
    private String province;
    private String city;
    private String district;
    private String streetAddress;
}
