package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Data
@Builder
@TableName("contact_us")
public class ContactUsEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String userPhone;
    private String content;
    private Date gmtCreated;
    private Date gmtModified;
    private Short deleted;

}
