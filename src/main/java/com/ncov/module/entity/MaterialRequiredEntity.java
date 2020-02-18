package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.Joiner;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.util.ImageUtils;
import com.ncov.module.controller.request.material.MaterialRequest;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 物资寻求 数据表的实体对象
 *
 * @author lucas
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("material_required")
public class MaterialRequiredEntity {
    private Long id;
    /**
     * 物资寻求联系人姓名
     */
    private String materialRequiredContactorName;
    /**
     * 物资寻求联系人手机号
     */
    private String materialRequiredContactorPhone;
    /**
     * 物资寻求收货地址
     */
    private String country;
    private String province;
    private String city;
    private String district;
    private String streetAddress;

    /**
     * 物资寻求
     */
    private Double materialRequiredQuantity;
    /**
     * 物资id
     */
    private Long materialId;
    /**
     * 物资寻求组织id
     */
    private Long materialRequiredOrganizationId;
    /**
     * 物资寻求
     */
    private Long materialRequiredUserId;
    /**
     * 物资寻求备注
     */
    private String materialRequiredComment;
    /**
     * 图片地址
     */
    private String materialRequiredImageUrls;
    /**
     * 物资名称
     */
    private String materialRequiredName;
    /**
     * 物资类别
     */
    private String materialRequiredCategory;
    /**
     * 执行标准
     */
    private String materialRequiredStandard;
    /**
     * 供货方机构名称
     */
    private String materialRequiredOrganizationName;

    /**
     * 物资寻求状态
     */
    private String materialRequiredStatus;

    private String reviewMessage;

    private Date gmtCreated;
    private Date gmtModified;
    private Short deleted;


    /**
     * 把请求带过来的对象转为实体集合
     *
     * @param materialRequest
     * @param organizationId
     * @param userId
     * @return
     */
    public static List<MaterialRequiredEntity> createList(MaterialRequest materialRequest, Long organizationId, Long userId) {
        return materialRequest.getMaterials().stream().map(material -> MaterialRequiredEntity.builder()
                .country(materialRequest.getAddress().getCountry())
                .province(materialRequest.getAddress().getProvince())
                .city(materialRequest.getAddress().getCity())
                .district(materialRequest.getAddress().getDistrict())
                .streetAddress(materialRequest.getAddress().getStreetAddress())
                .materialRequiredStatus(MaterialStatus.PENDING.name())
                .materialRequiredContactorName(materialRequest.getContactorName())
                .materialRequiredContactorPhone(materialRequest.getContactorPhone())
                .materialRequiredOrganizationName(materialRequest.getOrganisationName())
                .materialRequiredComment(materialRequest.getComment())
                .materialRequiredImageUrls(Joiner.on(",").join(materialRequest.getImageUrls()))
                .materialRequiredOrganizationId(organizationId)
                .materialRequiredUserId(userId)
                .materialRequiredName(material.getName())
                .materialRequiredCategory(material.getCategory())
                .materialRequiredQuantity(material.getQuantity())
                .materialRequiredStandard(material.getStandard())
                .gmtCreated(new Date()).build()
        ).collect(Collectors.toList());

    }

    public List<String> getImageUrls() {
        return ImageUtils.splitImageUrls(getMaterialRequiredImageUrls());
    }

    public void approve() {
        setMaterialRequiredStatus(MaterialStatus.PUBLISHED.name());
        setReviewMessage("");
        setGmtModified(new Date());
    }

    public void reject(String message) {
        setMaterialRequiredStatus(MaterialStatus.PENDING.name());
        setReviewMessage(message);
        setGmtModified(new Date());
    }
}
