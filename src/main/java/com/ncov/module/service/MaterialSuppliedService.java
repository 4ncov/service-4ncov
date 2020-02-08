package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialSuppliedEntity;
import com.ncov.module.mapper.MaterialSuppliedMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Slf4j
public class MaterialSuppliedService extends ServiceImpl<MaterialSuppliedMapper, MaterialSuppliedEntity> {

    @Autowired
    private MaterialSuppliedMapper materialSuppliedMapper;

    /**
     * 根据相关条件，查询物料供应分页列表
     *
     * @return
     */
    public com.ncov.module.controller.resp.Page<MaterialResponse> getSuppliedPageList(
            Integer pageNum, Integer pageSize, String category) {
        IPage<MaterialSuppliedEntity> result = materialSuppliedMapper.selectPage(
                new Page<MaterialSuppliedEntity>()
                        .setCurrent(pageNum)
                        .setSize(pageSize),
                isNotEmpty(category) ? new LambdaQueryWrapper<MaterialSuppliedEntity>()
                        .eq(MaterialSuppliedEntity::getMaterialSuppliedCategory, category) : null);
        com.ncov.module.controller.resp.Page<MaterialResponse> page = new com.ncov.module.controller.resp.Page<>();
        page.setData(this.carry(result.getRecords()));
        page.setPage(pageNum);
        page.setTotal(result.getTotal());
        page.setPageSize(pageSize);
        return page;
    }

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


    private List<MaterialResponse> carry(List<MaterialSuppliedEntity> source) {
        return source.stream()
                .map(material -> MaterialResponse.builder()
                        .address(material.getMaterialSuppliedAddress())
                        .comment(material.getMaterialSuppliedComment())
                        .contactorName(material.getMaterialSuppliedContactorName())
                        .contactorPhone(material.getMaterialSuppliedContactorPhone())
                        .gmtCreated(material.getGmtCreated())
                        .gmtModified(material.getGmtModified())
                        .id(material.getId())
                        .material(MaterialDto.builder()
                                .name(material.getMaterialSuppliedName())
                                .quantity(material.getMaterialSuppliedQuantity())
                                .standard(material.getMaterialSuppliedStandard())
                                .category(material.getMaterialSuppliedCategory())
                                .build())
                        .organisationName(material.getMaterialSuppliedOrganizationName())
                        .status(material.getMaterialSuppliedStatus())
                        .imageUrls(material.getImageUrls())
                        .build())
                .collect(Collectors.toList());
    }
}
