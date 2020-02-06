package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

/**
 * 物资寻求 数据表的实体对象
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
    private String materialRequiredReceivedAddress;
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
    private String materialSuppliedImageUrls;
    /**
     * 物资名称
     */
    private String materialSuppliedName;
    /**
     * 物资类别
     */
    private String materialSuppliedCategory;
    /**
     * 执行标准
     */
    private String materialSuppliedStandard;
    /**
     * 供货方机构名称
     */
    private String materialSuppliedOrganizationName;

    /**
     * 物资寻求状态
     */
    private String materialRequireStatus;

    private Date gmtCreated;
    private Date gmtModified;



}
