package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.exception.MaterialNotFoundException;
import com.ncov.module.common.util.ImageUtils;
import com.ncov.module.controller.dto.AddressDto;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.HospitalInfoEntity;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.entity.SupplierInfoEntity;
import com.ncov.module.entity.UserInfoEntity;
import com.ncov.module.mapper.MaterialRequiredMapper;
import com.ncov.module.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * 物料寻求服务
 *
 * @author lucas
 */
@Slf4j
@Service
public class MaterialRequiredService extends AbstractService<MaterialRequiredMapper, MaterialRequiredEntity> {

    @Autowired
    private MaterialRequiredMapper materialRequiredMapper;
    @Autowired
    private UserInfoService userInfoService;
    @Autowired
    private HospitalService hospitalService;
    @Autowired
    private UserContext userContext;

    /**
     * 根据相关条件，查询物料寻求分页列表
     *
     * @return
     */
    public com.ncov.module.controller.resp.Page<MaterialResponse> getRequiredPageList(
            Integer pageNum, Integer pageSize, String category) {
        LambdaQueryWrapper<MaterialRequiredEntity> queryWrapper = new LambdaQueryWrapper<MaterialRequiredEntity>()
                .ne(MaterialRequiredEntity::getMaterialRequiredStatus, MaterialStatus.PENDING.name())
                .orderByDesc(MaterialRequiredEntity::getGmtCreated);
        if (isNotEmpty(category)) {
            queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredCategory, category);
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
        UserInfoEntity user = userInfoService.getUser(userId);
        if (user.isVerified()) {
            materialRequiredEntities.forEach(MaterialRequiredEntity::approve);
        }
        saveBatch(materialRequiredEntities);
        return carry(materialRequiredEntities);
    }

    @Transactional(rollbackFor = Exception.class)
    public MaterialResponse update(Long materialId, MaterialRequest material) {
        MaterialRequiredEntity presentMaterial = getById(materialId);
        if (!isUpdateAllowed(presentMaterial)) {
            throw new AccessDeniedException("permission denied!");
        }
        MaterialDto materialDto = material.getMaterials().get(0);
        AddressDto address = material.getAddress();
        MaterialRequiredEntity updatedMaterial = presentMaterial.toBuilder()
                .materialRequiredContactorName(material.getContactorName())
                .materialRequiredContactorPhone(material.getContactorPhone())
                .country(address.getCountry())
                .province(address.getProvince())
                .city(address.getCity())
                .district(address.getDistrict())
                .streetAddress(address.getStreetAddress())
                .materialRequiredQuantity(materialDto.getQuantity())
                .materialRequiredComment(material.getComment())
                .materialRequiredImageUrls(ImageUtils.joinImageUrls(materialDto.getImageUrls()))
                .materialRequiredName(materialDto.getName())
                .materialRequiredCategory(materialDto.getCategory())
                .materialRequiredStandard(materialDto.getStandard())
                .gmtModified(new Date())
                .build();
        updateById(updatedMaterial);

        HospitalInfoEntity hospitalInfoEntity = hospitalService.getById(updatedMaterial.getMaterialRequiredOrganizationId());

        return carry(updatedMaterial, hospitalInfoEntity.getLogo());
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

    public MaterialResponse getDetail(Long id) {
        MaterialRequiredEntity materialRequiredEntity = getById(id);
        HospitalInfoEntity hospitalInfoEntity = hospitalService.getById(materialRequiredEntity.getMaterialRequiredOrganizationId());
        return carry(materialRequiredEntity, hospitalInfoEntity.getLogo());
    }

    private boolean isUpdateAllowed(MaterialRequiredEntity material) {
        return userContext.isSysAdmin() || userContext.getUserId().equals(material.getMaterialRequiredUserId());
    }

    private LambdaQueryWrapper<MaterialRequiredEntity> getFilterQueryWrapper(String category,
                                                                             String status,
                                                                             String contactPhone,
                                                                             Long userId) {
        LambdaQueryWrapper<MaterialRequiredEntity> queryWrapper = new LambdaQueryWrapper<MaterialRequiredEntity>()
                .orderByDesc(MaterialRequiredEntity::getGmtCreated);
        if (isNotEmpty(category)) {
            queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredCategory, category);
        }
        if (isNotEmpty(status)) {
            queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredStatus, status);
        }
        if (isNotEmpty(contactPhone)) {
            queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredContactorPhone, contactPhone);
        }
        if (Objects.nonNull(userId)) {
            queryWrapper.eq(MaterialRequiredEntity::getMaterialRequiredUserId, userId);
        }
        return queryWrapper;
    }

    private MaterialRequiredEntity getById(Long id) {
        return Optional.ofNullable(materialRequiredMapper.selectById(id))
                .orElseThrow(MaterialNotFoundException::new);
    }

    private List<MaterialResponse> carry(List<MaterialRequiredEntity> source) {
        List<Long> oids = source.stream().map(MaterialRequiredEntity::getMaterialRequiredOrganizationId).collect(Collectors.toList());
        List<HospitalInfoEntity> hospitalInfoEntityList = hospitalService.list(new LambdaQueryWrapper<HospitalInfoEntity>().in(HospitalInfoEntity::getId, oids));
        Map<Long, String> hospitalInfoEntityMap = hospitalInfoEntityList.stream().collect(Collectors.toMap(HospitalInfoEntity::getId, HospitalInfoEntity::getLogo));
        return source.stream()
                .map(material -> this.carry(material, hospitalInfoEntityMap.get(material.getMaterialRequiredOrganizationId())))
                .collect(Collectors.toList());
    }

    private MaterialResponse carry(MaterialRequiredEntity material, String logo) {
        return MaterialResponse.builder()
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
                .organisationLogo(logo)
                .status(material.getMaterialRequiredStatus())
                .reviewMessage(material.getReviewMessage())
                .build();
    }
}
