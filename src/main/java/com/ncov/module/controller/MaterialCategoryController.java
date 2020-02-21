package com.ncov.module.controller;

import com.ncov.module.controller.resp.RestResponse;
import com.ncov.module.controller.resp.category.MaterialCategoryResponse;
import com.ncov.module.service.MaterialCategoryService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/material-categories")
public class MaterialCategoryController {

    private final MaterialCategoryService materialCategoryService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public RestResponse<List<MaterialCategoryResponse>> getAll() {
        return RestResponse.getResp(
                "All categories listed.", materialCategoryService.getAllMaterialCategories());
    }
}
