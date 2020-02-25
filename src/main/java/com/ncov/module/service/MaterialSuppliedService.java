package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
import com.ncov.module.common.util.ImageUtils;
import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.entity.MaterialSuppliedEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.MaterialSuppliedMapper;
import com.ncov.module.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Service
@Slf4j
public class MaterialSuppliedService extends ServiceImpl<MaterialSuppliedMapper, MaterialSuppliedEntity> {

    @Autowired
    private MaterialSuppliedMapper materialSuppliedMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private SupplierService supplierService;
    @Autowired
    private UserContext userContext;

    private Map<Long, String> supplierInfoEntityMap = null;

    /**
     * 根据相关条件，查询物料供应分页列表
     *
     * @return
     */
    public com.ncov.module.controller.resp.Page<MaterialResponse> getSuppliedPageList(
            Integer pageNum, Integer pageSize, String category) {
        LambdaQueryWrapper<MaterialSuppliedEntity> queryWrapper = new LambdaQueryWrapper<MaterialSuppliedEntity>()
                .ne(MaterialSuppliedEntity::getMaterialSuppliedStatus, MaterialStatus.PENDING.name())
                .orderByDesc(MaterialSuppliedEntity::getGmtCreated);
        if (isNotEmpty(category)) {
            queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedCategory, category);
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
        UserInfoEntity user = userInfoService.getUser(userId);
        if (user.isVerified()) {
            materials.forEach(MaterialSuppliedEntity::approve);
        }
        saveBatch(materials);
        return carry(materials);
    }

    @Transactional(rollbackFor = Exception.class)
    public MaterialResponse update(Long materialId, MaterialRequest material) {
        MaterialSuppliedEntity presentMaterial = getById(materialId);
        if (!isUpdateAllowed(presentMaterial)) {
            throw new AccessDeniedException("permission denied!");
        }
        MaterialDto materialDto = material.getMaterials().get(0);
        AddressDto address = material.getAddress();
        MaterialSuppliedEntity updatedMaterial = presentMaterial.toBuilder()
                .materialSuppliedName(materialDto.getName())
                .materialSuppliedCategory(materialDto.getCategory())
                .materialSuppliedStandard(materialDto.getStandard())
                .materialSuppliedContactorName(material.getContactorName())
                .materialSuppliedContactorPhone(material.getContactorPhone())
                .country(address.getCountry())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .streetAddress(address.getStreetAddress())
                .materialSuppliedQuantity(materialDto.getQuantity())
                .materialSuppliedOrganizationName(material.getOrganisationName())
                .materialSuppliedComment(material.getComment())
                .materialSuppliedImageUrls(ImageUtils.joinImageUrls(materialDto.getImageUrls()))
                .gmtModified(new Date())
                .build();
        updateById(updatedMaterial);
        return carry(updatedMaterial);
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

    public MaterialResponse getDetail(Long id) {
        MaterialSuppliedEntity materialSuppliedEntity = getById(id);
        SupplierInfoEntity supplierInfoEntity = supplierService.getById(materialSuppliedEntity.getMaterialSupplierOrganizationId());
        supplierInfoEntityMap.put(supplierInfoEntity.getId(), supplierInfoEntity.getLogo());
        return carry(materialSuppliedEntity);
    }

    private boolean isUpdateAllowed(MaterialSuppliedEntity material) {
        return userContext.isSysAdmin() || userContext.getUserId().equals(material.getMaterialSuppliedUserId());
    }

    private LambdaQueryWrapper<MaterialSuppliedEntity> getFilterQueryWrapper(String category,
                                                                             String status,
                                                                             String contactPhone,
                                                                             Long userId) {
        LambdaQueryWrapper<MaterialSuppliedEntity> queryWrapper = new LambdaQueryWrapper<MaterialSuppliedEntity>()
                .orderByDesc(MaterialSuppliedEntity::getGmtCreated);
        if (isNotEmpty(category)) {
            queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedCategory, category);
        }
        if (isNotEmpty(status)) {
            queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedStatus, status);
        }
        if (isNotEmpty(contactPhone)) {
            queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedContactorPhone, contactPhone);
        }
        if (Objects.nonNull(userId)) {
            queryWrapper.eq(MaterialSuppliedEntity::getMaterialSuppliedUserId, userId);
        }
        return queryWrapper;
    }

    private MaterialSuppliedEntity getById(Long id) {
        return Optional.ofNullable(materialSuppliedMapper.selectById(id))
                .orElseThrow(MaterialNotFoundException::new);
    }

    private List<MaterialResponse> carry(List<MaterialSuppliedEntity> source) {
        List<Long> oids = source.stream().map(MaterialSuppliedEntity::getMaterialSupplierOrganizationId).collect(Collectors.toList());
        List<SupplierInfoEntity> supplierInfoEntityList = supplierService.list(new LambdaQueryWrapper<SupplierInfoEntity>().in(SupplierInfoEntity::getId, oids));
        supplierInfoEntityMap = supplierInfoEntityList.stream().collect(Collectors.toMap(SupplierInfoEntity::getId, SupplierInfoEntity::getLogo));
        return source.stream()
                .map(this::carry)
                .collect(Collectors.toList());
    }

    private MaterialResponse carry(MaterialSuppliedEntity material) {
        return MaterialResponse.builder()
                .address(AddressDto.builder()
                        .country(material.getCountry())
                        .province(material.getProvince())
                        .city(material.getCity())
                        .district(material.getDistrict())
                        .streetAddress(material.getStreetAddress())
                        .build())
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
                        .imageUrls(material.getImageUrls())
                        .build())
                .organisationName(material.getMaterialSuppliedOrganizationName())
                .organisationLogo(supplierInfoEntityMap != null?supplierInfoEntityMap.get(material.getMaterialSupplierOrganizationId()):"")
                .status(material.getMaterialSuppliedStatus())
                .reviewMessage(material.getReviewMessage())
                .build();
    }
}
