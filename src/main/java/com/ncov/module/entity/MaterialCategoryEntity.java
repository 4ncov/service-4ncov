package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("material_category")
public class MaterialCategoryEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String unit;
    private Date gmtCreated;
}
