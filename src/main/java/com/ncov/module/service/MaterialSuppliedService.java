package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
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
import java.util.Objects;
import java.util.Optional;
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
        LambdaQueryWrapper<MaterialSuppliedEntity> queryWrapper = new LambdaQueryWrapper<MaterialSuppliedEntity>()
                .ne(MaterialSuppliedEntity::getMaterialSuppliedStatus, MaterialStatus.PENDING.name());
        if (isNotEmpty(category)) {
            queryWrapper = queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedCategory, category);
        }
        Page<MaterialSuppliedEntity> results = materialSuppliedMapper.selectPage(
                new Page<MaterialSuppliedEntity>().setCurrent(pageNum).setSize(pageSize),
                queryWrapper);
        return com.ncov.module.controller.resp.Page.<MaterialResponse>builder()
                .data(carry(results.getRecords()))
                .page(pageNum)
                .pageSize(pageSize)
                .total(results.getTotal())
                .build();
    }

    @Transactional(rollbackFor = Exception.class)
    public List<MaterialResponse> create(MaterialRequest materialRequest, Long organisationId, Long userId) {
        List<MaterialSuppliedEntity> materials = MaterialSuppliedEntity.create(materialRequest, organisationId, userId);
        saveBatch(materials);
        return carry(materials);
    }

    public void approve(Long id) {
        MaterialSuppliedEntity material = getById(id);
        material.approve();
        updateById(material);
    }

    public void reject(Long id, String message) {
        MaterialSuppliedEntity material = getById(id);
        material.reject(message);
        updateById(material);
    }

    public com.ncov.module.controller.resp.Page<MaterialResponse> getAllSuppliedMaterialsPage(
            Integer page, Integer size, String category, String status, String contactPhone, Long userId) {
        Page<MaterialSuppliedEntity> results = materialSuppliedMapper.selectPage(
                new Page<MaterialSuppliedEntity>().setCurrent(page).setSize(size),
                getFilterQueryWrapper(category, status, contactPhone, userId)
        );
        return com.ncov.module.controller.resp.Page.<MaterialResponse>builder()
                .data(carry(results.getRecords()))
                .page(page)
                .pageSize(size)
                .total(results.getTotal())
                .build();
    }

    private LambdaQueryWrapper<MaterialSuppliedEntity> getFilterQueryWrapper(String category,
                                                                             String status,
                                                                             String contactPhone,
                                                                             Long userId) {
        LambdaQueryWrapper<MaterialSuppliedEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (isNotEmpty(category)) {
            queryWrapper = queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedCategory, category);
        }
        if (isNotEmpty(status)) {
            queryWrapper = queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedStatus, status);
        }
        if (isNotEmpty(contactPhone)) {
            queryWrapper = queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedContactorPhone, contactPhone);
        }
        if (Objects.nonNull(userId)) {
            queryWrapper = queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedUserId, userId);
        }
        return queryWrapper;
    }

    private MaterialSuppliedEntity getById(Long id) {
        return Optional.ofNullable(materialSuppliedMapper.selectById(id))
                .orElseThrow(MaterialNotFoundException::new);
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
                        .id(material.getId().toString())
                        .material(MaterialDto.builder()
                                .name(material.getMaterialSuppliedName())
                                .quantity(material.getMaterialSuppliedQuantity())
                                .standard(material.getMaterialSuppliedStandard())
                                .category(material.getMaterialSuppliedCategory())
                                .build())
                        .organisationName(material.getMaterialSuppliedOrganizationName())
                        .status(material.getMaterialSuppliedStatus())
                        .imageUrls(material.getImageUrls())
                        .reviewMessage(material.getReviewMessage())
                        .build())
                .collect(Collectors.toList());
    }
}
