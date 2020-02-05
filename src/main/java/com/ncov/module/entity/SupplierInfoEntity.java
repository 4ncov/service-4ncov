package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncov.module.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("material_supplier_info")
public class SupplierInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String materialSupplierName;
    private String materialSupplierContactorName;
    private String materialSupplierVerifyImageUrls;
    private Long materialSupplierCreatorUserId;
    private String materialSupplierContactorPhone;
    private Boolean haveLogistics;
    private Date gmtCreated;
    private Date gmtModified;

    public List<String> getAllImageUrls() {
        if (StringUtils.isEmpty(getMaterialSupplierVerifyImageUrls())) {
            return Collections.emptyList();
        }
        return Arrays.stream(getMaterialSupplierVerifyImageUrls().split(Constants.DELIMITER_COMMA))
                .collect(Collectors.toList());
    }

    public void setMaterialSupplierVerifyImageUrls(String materialSupplierVerifyImageUrls) {
        this.materialSupplierVerifyImageUrls = materialSupplierVerifyImageUrls;
    }

    public void setMaterialSupplierVerifyImageUrls(List<String> imageUrls) {
        if (CollectionUtils.isEmpty(imageUrls)) {
            setMaterialSupplierVerifyImageUrls("");
            return;
        }
        setMaterialSupplierVerifyImageUrls(String.join(Constants.DELIMITER_COMMA, imageUrls));
    }
}