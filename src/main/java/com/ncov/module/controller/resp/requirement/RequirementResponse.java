package com.ncov.module.controller.resp.requirement;

import com.ncov.module.controller.dto.CommodityDto;
import com.ncov.module.controller.dto.RequesterDto;
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
public class RequirementResponse {

    private Long id;
    private List<CommodityDto> commodities;
    private RequesterDto requester;
    private String requirementImageUrl;
    private String comment;
    private String status;
    private Date createdTime;
    private Date updatedTime;
}
