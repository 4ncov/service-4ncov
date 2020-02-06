package com.ncov.module.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Joiner;
import com.ncov.module.controller.dto.MaterialDto;
import com.ncov.module.controller.request.material.MaterialRequest;
import com.ncov.module.controller.resp.material.MaterialResponse;
import com.ncov.module.entity.MaterialRequiredEntity;
import com.ncov.module.mapper.MaterialRequiredMapper;
import com.ncov.module.security.UserContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 物料寻求服务
 * @author lucas
 */
@Slf4j
@Service
public class MaterialRequiredService extends ServiceImpl<MaterialRequiredMapper, MaterialRequiredEntity> {

    @Autowired
    private MaterialRequiredMapper materialRequiredMapper;
    @Autowired
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
                        .like(MaterialRequiredEntity::getMaterialSuppliedCategory, category));
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
    public MaterialRequiredEntity saveRequiredInfo(MaterialRequest materialRequest){
        MaterialRequiredEntity materialRequiredEntity = MaterialRequiredEntity.builder()
                .materialRequiredComment(materialRequest.getComment())
                .materialRequiredContactorName(materialRequest.getContactorName())
                .materialRequiredContactorPhone(materialRequest.getContactorPhone())
                .materialRequiredOrganizationId(1L)
                .materialRequiredUserId(1L)
                .materialSuppliedOrganizationName(materialRequest.getOrganisationName())
                .materialRequireStatus("PUBLISHED")
                .materialRequiredReceivedAddress(materialRequest.getAddress())
                .materialSuppliedImageUrls(Joiner.on(",").join(materialRequest.getImageUrls()))
                .gmtCreated(new Date()).build();
        materialRequest.getMaterials().stream().forEach(item->{
            materialRequiredEntity.setMaterialSuppliedCategory(item.getCategory());
            materialRequiredEntity.setMaterialSuppliedName(item.getName());
            materialRequiredEntity.setMaterialSuppliedStandard(item.getStandard());
            materialRequiredEntity.setMaterialRequiredQuantity(item.getQuantity());
            materialRequiredMapper.insert(materialRequiredEntity);
        });
        return materialRequiredEntity;
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
