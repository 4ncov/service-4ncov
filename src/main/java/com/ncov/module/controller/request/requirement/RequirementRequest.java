package com.ncov.module.controller.request.requirement;

import com.ncov.module.controller.dto.CommodityDto;
import com.ncov.module.controller.dto.RequesterDto;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequirementRequest {

    private List<CommodityDto> commodities;
    private RequesterDto requester;
    private String requirementImageUrl;
    private String comment;
}
