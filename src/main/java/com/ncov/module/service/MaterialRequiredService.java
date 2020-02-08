package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
        IPage<MaterialRequiredEntity> result = materialRequiredMapper.selectPage(
                new Page<MaterialRequiredEntity>()
                        .setCurrent(pageNum)
                        .setSize(pageSize),
                isNotEmpty(category) ? new LambdaQueryWrapper<MaterialRequiredEntity>()
                        .eq(MaterialRequiredEntity::getMaterialRequiredCategory, category) : null);
        com.ncov.module.controller.resp.Page<MaterialResponse> page = new com.ncov.module.controller.resp.Page<>();
        page.setData(this.carry(result.getRecords()));
        page.setPage(pageNum);
        page.setTotal(result.getTotal());
        page.setPageSize(pageSize);
        return page;
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
        return materialRequiredEntities.stream().map(materialRequiredEntity ->
                MaterialResponse.builder()
                        .id(materialRequiredEntity.getId())
                        .address(materialRequiredEntity.getMaterialRequiredReceivedAddress())
                        .comment(materialRequiredEntity.getMaterialRequiredComment())
                        .contactorName(materialRequiredEntity.getMaterialRequiredContactorName())
                        .contactorPhone(materialRequiredEntity.getMaterialRequiredContactorPhone())
                        .gmtCreated(materialRequiredEntity.getGmtCreated())
                        .organisationName(materialRequiredEntity.getMaterialRequiredOrganizationName())
                        .status(materialRequiredEntity.getMaterialRequiredStatus())
                        .material(MaterialDto.builder()
                                .category(materialRequiredEntity.getMaterialRequiredCategory())
                                .name(materialRequiredEntity.getMaterialRequiredName())
                                .standard(materialRequiredEntity.getMaterialRequiredStandard())
                                .quantity(materialRequiredEntity.getMaterialRequiredQuantity()).build())
                        .imageUrls(materialRequiredEntity.getImageUrls())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<MaterialResponse> carry(List<MaterialRequiredEntity> source) {
        return source.stream()
                .map(material -> MaterialResponse.builder()
                        .address(material.getMaterialRequiredReceivedAddress())
                        .comment(material.getMaterialRequiredComment())
                        .contactorName(material.getMaterialRequiredContactorName())
                        .contactorPhone(material.getMaterialRequiredContactorPhone())
                        .gmtCreated(material.getGmtCreated())
                        .gmtModified(material.getGmtModified())
                        .id(material.getId())
                        .material(MaterialDto.builder()
                                .category(material.getMaterialRequiredCategory())
                                .standard(material.getMaterialRequiredStandard())
                                .quantity(material.getMaterialRequiredQuantity())
                                .name(material.getMaterialRequiredName())
                                .build())
                        .organisationName(material.getMaterialRequiredOrganizationName())
                        .status(material.getMaterialRequiredStatus())
                        .imageUrls(material.getImageUrls())
                        .build())
                .collect(Collectors.toList());
    }
}
