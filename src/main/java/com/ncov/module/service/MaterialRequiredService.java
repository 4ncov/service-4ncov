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
import com.ncov.module.security.UserContext;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物料寻求服务
 * @author lucas
 */
@Slf4j
@Service
@AllArgsConstructor
public class MaterialRequiredService extends ServiceImpl<MaterialRequiredMapper, MaterialRequiredEntity> {

    private MaterialRequiredMapper materialRequiredMapper;
    private UserContext userContext;

    /**
     * 根据相关条件，查询物料寻求分页列表
     * @return
     */
    public com.ncov.module.controller.resp.Page<MaterialResponse> getRequiredPageList(int pageSize, int pageNums, String category){
        IPage<MaterialRequiredEntity> result = materialRequiredMapper.selectPage(
                new Page<MaterialRequiredEntity>()
                        .setPages(pageSize)
                        .setSize(pageNums),
                new LambdaQueryWrapper<MaterialRequiredEntity>()
                        .eq(MaterialRequiredEntity::getMaterialSuppliedCategory, category));
        com.ncov.module.controller.resp.Page<MaterialResponse> page = new com.ncov.module.controller.resp.Page<>();
        page.setData(this.carry(result.getRecords()));
        page.setPage(result.getPages());
        page.setTotal(result.getTotal());
        page.setPageSize(result.getSize());
        return page;
    }

    /**
     * 保存物料寻求信息
     * @param materialRequest
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public List<MaterialResponse> saveRequiredInfo(MaterialRequest materialRequest){
        List<MaterialRequiredEntity> materialRequiredEntities = MaterialRequiredEntity.createList(materialRequest, userContext.getOrganisationId(), userContext.getUserId());
        saveBatch(materialRequiredEntities);
        return materialRequiredEntities.stream().map(materialRequiredEntity ->
                MaterialResponse.builder()
                        .id(materialRequiredEntity.getId())
                        .address(materialRequiredEntity.getMaterialRequiredReceivedAddress())
                        .comment(materialRequiredEntity.getMaterialRequiredComment())
                        .contactorName(materialRequiredEntity.getMaterialRequiredContactorName())
                        .contactorPhone(materialRequiredEntity.getMaterialRequiredContactorPhone())
                        .gmtCreated(materialRequiredEntity.getGmtCreated())
                        .organisationName(materialRequiredEntity.getMaterialSuppliedOrganizationName())
                        .status(materialRequiredEntity.getMaterialRequireStatus())
                        .material(MaterialDto.builder()
                                .category(materialRequiredEntity.getMaterialSuppliedCategory())
                                .name(materialRequiredEntity.getMaterialSuppliedName())
                                .standard(materialRequiredEntity.getMaterialSuppliedStandard())
                                .quantity(materialRequiredEntity.getMaterialRequiredQuantity()).build())
                        .build()
        ).collect(Collectors.toList());
    }

    private List<MaterialResponse> carry(List<MaterialRequiredEntity> source){
        List<MaterialResponse> target = new ArrayList<>();
        int size = source.size();
        for (int i = 0; i < size; i++) {
            MaterialResponse materialResponse = new MaterialResponse();
            materialResponse.setAddress(source.get(i).getMaterialRequiredReceivedAddress());
            materialResponse.setComment(source.get(i).getMaterialRequiredComment());
            materialResponse.setContactorName(source.get(i).getMaterialRequiredContactorName());
            materialResponse.setContactorPhone(source.get(i).getMaterialRequiredContactorPhone());
            materialResponse.setGmtCreated(source.get(i).getGmtCreated());
            materialResponse.setGmtModified(source.get(i).getGmtModified());
            materialResponse.setId(source.get(i).getId());
            MaterialDto materialDto = new MaterialDto();
            materialDto.setCategory(source.get(i).getMaterialSuppliedCategory());
            materialDto.setName(source.get(i).getMaterialSuppliedName());
            materialDto.setQuantity(source.get(i).getMaterialRequiredQuantity());
            materialDto.setStandard(source.get(i).getMaterialSuppliedStandard());
            materialResponse.setMaterial(materialDto);
            materialResponse.setOrganisationName(source.get(i).getMaterialSuppliedOrganizationName());
            materialResponse.setStatus(source.get(i).getMaterialRequireStatus());
            target.add(materialResponse);
        }
        return target;
    }
}
