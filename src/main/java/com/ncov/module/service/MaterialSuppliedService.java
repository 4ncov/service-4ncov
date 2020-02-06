package com.ncov.module.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialSuppliedEntity;
import com.ncov.module.mapper.MaterialSuppliedMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MaterialSuppliedService extends ServiceImpl<MaterialSuppliedMapper, MaterialSuppliedEntity> {

    @Transactional(rollbackFor = Exception.class)
    public List<MaterialResponse> create(MaterialRequest materialRequest, Long organisationId, Long userId) {
        List<MaterialSuppliedEntity> materials = MaterialSuppliedEntity.create(materialRequest, organisationId, userId);
        saveBatch(materials);
        return materials.stream()
                .map(material -> MaterialResponse.builder()
                        .id(material.getId())
                        .material(MaterialDto.builder()
                                .name(material.getMaterialSuppliedName())
                                .category(material.getMaterialSuppliedCategory())
                                .standard(material.getMaterialSuppliedStandard())
                                .quantity(material.getMaterialSuppliedQuantity())
                                .build())
                        .organisationName(material.getMaterialSuppliedOrganizationName())
                        .address(material.getMaterialSuppliedAddress())
                        .contactorName(material.getMaterialSuppliedContactorName())
                        .contactorPhone(material.getMaterialSuppliedContactorPhone())
                        .comment(material.getMaterialSuppliedComment())
                        .status(material.getMaterialSuppliedStatus())
                        .gmtCreated(material.getGmtCreated())
                        .gmtModified(material.getGmtModified())
                        .build())
                .collect(Collectors.toList());
    }
}
