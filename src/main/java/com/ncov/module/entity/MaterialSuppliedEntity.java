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
@Builder
@TableName("material_supplied")
public class MaterialSuppliedEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String materialSuppliedName;
    private String materialSuppliedCategory;
    private String materialSuppliedStandard;
    private String materialSuppliedContactorName;
    private String materialSuppliedContactorPhone;
    private String materialSuppliedAddress;
    private Double materialSuppliedQuantity;
    private Long materialId;
    private String materialSuppliedOrganizationName;
    private Long materialSupplierOrganizationId;
    private Long materialSuppliedUserId;
    private String materialSuppliedComment;
    private String materialSuppliedStatus;
    private String materialSuppliedImageUrls;
    private Date gmtCreated;
    private Date gmtModified;

    public static List<MaterialSuppliedEntity> create(MaterialRequest request,
                                                      Long organisationId, Long userId) {
        return request.getMaterials().stream()
                .map(material -> MaterialSuppliedEntity.builder()
                        .materialSuppliedName(material.getName())
                        .materialSuppliedCategory(material.getCategory())
                        .materialSuppliedStandard(material.getStandard())
                        .materialSuppliedContactorName(request.getContactorName())
                        .materialSuppliedContactorPhone(request.getContactorPhone())
                        .materialSuppliedAddress(request.getAddress())
                        .materialSuppliedQuantity(material.getQuantity())
                        .materialSuppliedOrganizationName(request.getOrganisationName())
                        .materialSupplierOrganizationId(organisationId)
                        .materialSuppliedUserId(userId)
                        .materialSuppliedComment(request.getComment())
                        .materialSuppliedStatus(MaterialStatus.PENDING.name())
                        .materialSuppliedImageUrls(ImageUtils.joinImageUrls(request.getImageUrls()))
                        .gmtCreated(new Date())
                        .build())
                .collect(Collectors.toList());
    }

    public List<String> getImageUrls() {
        return ImageUtils.splitImageUrls(getMaterialSuppliedImageUrls());
    }
}
