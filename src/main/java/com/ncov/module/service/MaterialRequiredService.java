package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.mapper.MaterialRequiredMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 物料寻求服务
 *
 * @author lucas
 */
@Slf4j
@Service
public class MaterialRequiredService extends ServiceImpl<MaterialRequiredMapper, MaterialRequiredEntity> {

    @Autowired
    private MaterialRequiredMapper materialRequiredMapper;

    /**
     * 根据相关条件，查询物料寻求分页列表
     *
     * @return
     */
    public com.ncov.module.controller.resp.Page<MaterialResponse> getRequiredPageList(
            Integer pageNum, Integer pageSize, String category) {
        LambdaQueryWrapper<MaterialRequiredEntity> queryWrapper = new LambdaQueryWrapper<MaterialRequiredEntity>()
                .ne(MaterialRequiredEntity::getMaterialRequiredStatus, MaterialStatus.PENDING.name());
        if (isNotEmpty(category)) {
            queryWrapper = queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredCategory, category);
        }

        Page<MaterialRequiredEntity> results = materialRequiredMapper.selectPage(
                new Page<MaterialRequiredEntity>().setCurrent(pageNum).setSize(pageSize),
                queryWrapper);
        return com.ncov.module.controller.resp.Page.<MaterialResponse>builder()
                .data(carry(results.getRecords()))
                .page(pageNum)
                .pageSize(pageSize)
                .total(results.getTotal())
                .build();
    }

    /**
     * 保存物料寻求信息
     *
     * @param materialRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<MaterialResponse> saveRequiredInfo(MaterialRequest materialRequest,
                                                   Long organisationId, Long userId) {
        List<MaterialRequiredEntity> materialRequiredEntities = MaterialRequiredEntity.createList(
                materialRequest, organisationId, userId);
        saveBatch(materialRequiredEntities);
        return carry(materialRequiredEntities);
    }

    public com.ncov.module.controller.resp.Page<MaterialResponse> getAllRequiredMaterialsPage(
            Integer page, Integer size, String category, String status, String contactPhone, Long userId) {
        Page<MaterialRequiredEntity> results = materialRequiredMapper.selectPage(
                new Page<MaterialRequiredEntity>().setCurrent(page).setSize(size),
                getFilterQueryWrapper(category, status, contactPhone, userId)
        );
        return com.ncov.module.controller.resp.Page.<MaterialResponse>builder()
                .data(carry(results.getRecords()))
                .page(page)
                .pageSize(size)
                .total(results.getTotal())
                .build();
    }

    public MaterialResponse update(MaterialRequest request) {
        // TODO
        return null;
    }

    public void approve(Long id) {
        MaterialRequiredEntity material = getById(id);
        material.approve();
        updateById(material);
    }

    public void reject(Long id, String message) {
        MaterialRequiredEntity material = getById(id);
        material.reject(message);
        updateById(material);
    }

    private LambdaQueryWrapper<MaterialRequiredEntity> getFilterQueryWrapper(String category,
                                                                             String status,
                                                                             String contactPhone,
                                                                             Long userId) {
        LambdaQueryWrapper<MaterialRequiredEntity> queryWrapper = new LambdaQueryWrapper<>();
        if (isNotEmpty(category)) {
            queryWrapper = queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredCategory, category);
        }
        if (isNotEmpty(status)) {
            queryWrapper = queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredStatus, status);
        }
        if (isNotEmpty(contactPhone)) {
            queryWrapper = queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredContactorPhone, contactPhone);
        }
        if (Objects.nonNull(userId)) {
            queryWrapper = queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredUserId, userId);
        }
        return queryWrapper;
    }

    private MaterialRequiredEntity getById(Long id) {
        return Optional.ofNullable(materialRequiredMapper.selectById(id))
                .orElseThrow(MaterialNotFoundException::new);
    }

    private List<MaterialResponse> carry(List<MaterialRequiredEntity> source) {
        return source.stream()
                .map(material -> MaterialResponse.builder()
                        .address(AddressDto.builder()
                                .country(material.getCountry())
                                .province(material.getProvince())
                                .city(material.getCity())
                                .district(material.getDistrict())
                                .streetAddress(material.getStreetAddress())
                                .build())
                        .comment(material.getMaterialRequiredComment())
                        .contactorName(material.getMaterialRequiredContactorName())
                        .contactorPhone(material.getMaterialRequiredContactorPhone())
                        .gmtCreated(material.getGmtCreated())
                        .gmtModified(material.getGmtModified())
                        .id(material.getId().toString())
                        .material(MaterialDto.builder()
                                .category(material.getMaterialRequiredCategory())
                                .standard(material.getMaterialRequiredStandard())
                                .quantity(material.getMaterialRequiredQuantity())
                                .name(material.getMaterialRequiredName())
                                .imageUrls(material.getImageUrls())
                                .build())
                        .organisationName(material.getMaterialRequiredOrganizationName())
                        .status(material.getMaterialRequiredStatus())
                        .reviewMessage(material.getReviewMessage())
                        .build())
                .collect(Collectors.toList());
    }
}
