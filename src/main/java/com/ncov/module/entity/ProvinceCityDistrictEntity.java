package com.ncov.module.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProvinceCityDistrictEntity {

    private String province;
    private String city;
    private String district;
}
