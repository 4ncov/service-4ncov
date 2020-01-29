package com.ncov.module.controller;

import com.ncov.module.common.SwaggerConstants;
import com.ncov.module.controller.request.supplement.SupplementRequest;
import com.ncov.module.controller.resp.Page;
import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.supplement.SupplementResponse;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/supplements")
public class SupplementController {

    @ApiOperation(
            value = "Create supplement.",
            tags = SwaggerConstants.TAG_SUPPLEMENTS
    )
    @PostMapping
    public ResponseEntity<RestResponse> create(@RequestBody SupplementRequest supplement) {
        // TODO: 2020-01-29
        return null;
    }

    @ApiOperation(
            value = "List supplements.",
            tags = SwaggerConstants.TAG_SUPPLEMENTS
    )
    @GetMapping
    public ResponseEntity<Page<SupplementResponse>> list(
            @RequestParam Integer page, @RequestParam Integer size,
            @RequestParam(name = "category", required = false) String category) {
        // TODO: 2020-01-29
        return null;
    }

    @ApiOperation(
            value = "List my supplements.",
            tags = SwaggerConstants.TAG_SUPPLEMENTS
    )
    @GetMapping("/me")
    public ResponseEntity<Page<SupplementResponse>> listMine(@RequestParam Integer page, @RequestParam Integer size) {
        // TODO: 2020-01-29
        return null;
    }
}
