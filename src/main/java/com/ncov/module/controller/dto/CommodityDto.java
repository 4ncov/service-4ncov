package com.ncov.module.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommodityDto {

    private String name;
    private Double quantity;
    private String standard;
    private String category;
}
