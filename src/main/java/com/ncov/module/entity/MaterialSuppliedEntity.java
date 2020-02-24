package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncov.module.common.enums.MaterialStatus;
import com.ncov.module.common.util.ImageUtils;
import com.ncov.module.controller.request.material.MaterialRequest;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@TableName("material_supplied")
public class MaterialSuppliedEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String materialSuppliedName;
    private String materialSuppliedCategory;
    private String materialSuppliedStandard;
    private String materialSuppliedContactorName;
    private String materialSuppliedContactorPhone;
    private String country;
    private String province;
    private String city;
    private String district;
    private String streetAddress;
    private Double materialSuppliedQuantity;
    private Long materialId;
    private String materialSuppliedOrganizationName;
    private Long materialSupplierOrganizationId;
    private Long materialSuppliedUserId;
    private String materialSuppliedComment;
    private String materialSuppliedStatus;
    private String materialSuppliedImageUrls;
    private String reviewMessage;
    private Date gmtCreated;
    private Date gmtModified;
    private Short deleted;

    public static List<MaterialSuppliedEntity> create(MaterialRequest request,
                                                      Long organisationId, Long userId) {
        return request.getMaterials().stream()
                .map(material -> MaterialSuppliedEntity.builder()
                        .materialSuppliedName(material.getName())
                        .materialSuppliedCategory(material.getCategory())
                        .materialSuppliedStandard(material.getStandard())
                        .materialSuppliedContactorName(request.getContactorName())
                        .materialSuppliedContactorPhone(request.getContactorPhone())
                        .country(request.getAddress().getCountry())
                        .province(request.getAddress().getProvince())
                        .city(request.getAddress().getCity())
                        .district(request.getAddress().getDistrict())
                        .streetAddress(request.getAddress().getStreetAddress())
                        .materialSuppliedQuantity(material.getQuantity())
                        .materialSuppliedOrganizationName(request.getOrganisationName())
                        .materialSupplierOrganizationId(organisationId)
                        .materialSuppliedUserId(userId)
                        .materialSuppliedComment(request.getComment())
                        .materialSuppliedStatus(MaterialStatus.PENDING.name())
                        .materialSuppliedImageUrls(ImageUtils.joinImageUrls(material.getImageUrls()))
                        .gmtCreated(new Date())
                        .build())
                .collect(Collectors.toList());
    }

    public List<String> getImageUrls() {
        return ImageUtils.splitImageUrls(getMaterialSuppliedImageUrls());
    }

    public void approve() {
        setMaterialSuppliedStatus(MaterialStatus.PUBLISHED.name());
        setReviewMessage("");
        setGmtModified(new Date());
    }

    public void reject(String message) {
        setMaterialSuppliedStatus(MaterialStatus.PENDING.name());
        setReviewMessage(message);
        setGmtModified(new Date());
    }

    public boolean isApproved() {
        return !MaterialStatus.PENDING.name().equals(getMaterialSuppliedStatus());
    }
}
