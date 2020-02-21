package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.security.UserContext;
import com.ncov.module.service.MaterialRequiredService;
import com.ncov.module.service.MaterialSuppliedService;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class MaterialsController {

    private final UserContext userContext;
    private final MaterialSuppliedService materialSuppliedService;
    private final MaterialRequiredService materialRequiredService;

    @ApiOperation(
            value = "Create new required material.",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @PreAuthorize("hasRole('ROLE_HOSPITAL') or hasRole('ROLE_SYSADMIN')")
    @PostMapping("/required-materials")
    @ResponseStatus(HttpStatus.CREATED)
    public RestResponse createRequiredMaterial(@RequestBody @Valid MaterialRequest material) {
        return RestResponse.getResp("保存成功", materialRequiredService.saveRequiredInfo(material,
                userContext.getOrganisationId(), userContext.getUserId()));
    }

    @ApiOperation(
            value = "Approve a required material.",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @PatchMapping("/required-materials/{id}:approve")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse approveRequiredMaterial(@PathVariable Long id) {
        materialRequiredService.approve(id);
        return RestResponse.getResp("保存成功");
    }

    @ApiOperation(
            value = "Reject a required material.",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @PatchMapping("/required-materials/{id}:reject")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse rejectRequiredMaterial(@PathVariable Long id, @RequestParam String reviewMessage) {
        materialRequiredService.reject(id, reviewMessage);
        return RestResponse.getResp("保存成功");
    }

    @ApiOperation(
            value = "List required materials.",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @GetMapping("/required-materials")
    @ResponseStatus(HttpStatus.OK)
    public Page<MaterialResponse> listRequiredMaterials(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category) {
        return materialRequiredService.getRequiredPageList(page, size, category);
    }

    @ApiOperation(
            value = "List all required materials (admin only).",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @GetMapping("/required-materials/all")
    @ResponseStatus(HttpStatus.OK)
    public Page<MaterialResponse> listAllRequiredMaterials(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "contactPhone", required = false) String contactPhone,
            @RequestParam(name = "userId", required = false) Long userId) {
        return materialRequiredService.getAllRequiredMaterialsPage(page, size, category, status, contactPhone, userId);
    }

    @ApiOperation(
            value = "List my required materials.",
            tags = SwaggerConstants.TAG_REQUIRED_MATERIALS
    )
    @GetMapping("/required-materials/me")
    @PreAuthorize("hasRole('ROLE_SYSADMIN') or hasRole('ROLE_HOSPITAL')")
    public Page<MaterialResponse> listMyRequiredMaterials(@RequestParam Integer page,
                                                          @RequestParam Integer size) {
        return materialRequiredService.getAllRequiredMaterialsPage(page
                , size
                , null
                , null
                , null
                , userContext.getUserId());
    }

    @ApiOperation(
            value = "Create new supplied material.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @PostMapping("/supplied-materials")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_SUPPLIER') or hasRole('ROLE_SYSADMIN')")
    public RestResponse<List<MaterialResponse>> createSuppliedMaterial(@RequestBody @Valid MaterialRequest material) {
        List<MaterialResponse> responses = materialSuppliedService.create(material,
                userContext.getOrganisationId(), userContext.getUserId());
        return RestResponse.getResp("Supplied materials created.", responses);
    }

    @ApiOperation(
            value = "Admin approve a supplied material.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @PatchMapping("/supplied-materials/{id}:approve")
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse approveSuppliedMaterial(@PathVariable Long id) {
        materialSuppliedService.approve(id);
        return RestResponse.getResp("保存成功");
    }

    @ApiOperation(
            value = "Admin reject a supplied material.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @PatchMapping("/supplied-materials/{id}:reject")
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RestResponse rejectSuppliedMaterial(@PathVariable Long id, @RequestParam String reviewMessage) {
        materialSuppliedService.reject(id, reviewMessage);
        return RestResponse.getResp("保存成功");
    }

    @ApiOperation(
            value = "List supplied materials.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @GetMapping("/supplied-materials")
    @ResponseStatus(HttpStatus.OK)
    public Page<MaterialResponse> listSuppliedMaterials(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category) {
        return materialSuppliedService.getSuppliedPageList(page, size, category);
    }

    @ApiOperation(
            value = "Admin list all supplied materials.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @GetMapping("/supplied-materials/all")
    @PreAuthorize("hasRole('ROLE_SYSADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public Page<MaterialResponse> listAllSuppliedMaterials(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "contactPhone", required = false) String contactPhone,
            @RequestParam(name = "userId", required = false) Long userId) {
        return materialSuppliedService.getAllSuppliedMaterialsPage(page, size, category, status, contactPhone, userId);
    }

    @ApiOperation(
            value = "List my supplied materials.",
            tags = SwaggerConstants.TAG_SUPPLIED_MATERIALS
    )
    @GetMapping("/supplied-materials/me")
    @PreAuthorize("hasRole('ROLE_SUPPLIER') or hasRole('ROLE_SYSADMIN')")
    public Page<MaterialResponse> listMySuppliedMaterials(@RequestParam Integer page,
                                                          @RequestParam Integer size) {
        return materialSuppliedService.getAllSuppliedMaterialsPage(page
                , size
                , null
                , null
                , null
                , userContext.getUserId());
    }
}
