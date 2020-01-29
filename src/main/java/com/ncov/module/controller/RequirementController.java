package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.requirement.RequirementRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.requirement.RequirementResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/requirements")
public class RequirementController {

    @ApiOperation(
            value = "Create new requirement.",
            tags = SwaggerConstants.TAG_REQUIREMENTS
    )
    @PostMapping
    public ResponseEntity<RestResponse> create(@RequestBody RequirementRequest requirement) {
        // TODO: 2020-01-29
        return null;
    }

    @ApiOperation(
            value = "List requirements.",
            tags = SwaggerConstants.TAG_REQUIREMENTS
    )
    @GetMapping
    public ResponseEntity<Page<RequirementResponse>> list(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category) {
        // TODO: 2020-01-29
        return null;
    }

    @ApiOperation(
            value = "List my requirements.",
            tags = SwaggerConstants.TAG_REQUIREMENTS
    )
    @GetMapping("/me")
    public ResponseEntity<Page<RequirementResponse>> listMine(@RequestParam Integer page, @RequestParam Integer size) {
        // TODO: 2020-01-29
        return null;
    }
}
