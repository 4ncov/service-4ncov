package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.controller.resp.category.MaterialCategoryResponse;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.mapper.MaterialCategoryMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class MaterialCategoryService {

    private final MaterialCategoryMapper materialCategoryMapper;

    public List<MaterialCategoryResponse> getAllMaterialCategories() {
        return materialCategoryMapper.selectAll().stream()
                .map(category -> MaterialCategoryResponse.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .unit(category.getUnit())
                        .build())
                .collect(Collectors.toList());
    }
}
