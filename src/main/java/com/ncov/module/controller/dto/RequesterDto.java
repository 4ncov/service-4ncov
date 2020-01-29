package com.ncov.module.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequesterDto {

    private String organisationName;
    private String contactName;
    private String phoneNumber;
    private String shippingAddress;
}
