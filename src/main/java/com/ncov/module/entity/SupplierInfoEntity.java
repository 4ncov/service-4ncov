package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("material_supplier_info")
public class SupplierInfoEntity {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private Date gmtCreated;

    private Date gmtModified;

    private String materialSupplierName;

    private String materialSupplierContactorName;

    private String materialSupplierUniformSocialCreditCode;

    private String materialSupplierCompanyAddress;
    @TableField(value = "material_supplier_verify_info_url_1")
    private String materialSupplierVerifyInfoUrl1;
    @TableField(value = "material_supplier_verify_info_url_2")
    private String materialSupplierVerifyInfoUrl2;
    @TableField(value = "material_supplier_verify_info_url_3")
    private String materialSupplierVerifyInfoUrl3;

    private Long materialSupplierCreatorUserId;

    private String materialSupplierContactorPhone;

    private Integer haveLogistics;

}