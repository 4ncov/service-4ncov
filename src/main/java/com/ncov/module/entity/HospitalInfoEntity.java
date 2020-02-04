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

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("hospital_info")
public class HospitalInfoEntity {
    @TableId(value="id", type= IdType.AUTO)
    private Long id;

    private String hospitalName;

    private Date gmtCreated;

    private Date gmtModified;

    private String hospitalUniformSocialCreditCode;

    private Long hospitalCreatorUserId;

    private String hospitalDetailAddress;
    @TableField(value = "hospital_verify_info_url_1")
    private String hospitalVerifyInfoUrl1;
    @TableField(value = "hospital_verify_info_url_2")
    private String hospitalVerifyInfoUrl2;
    @TableField(value = "hospital_verify_info_url_3")
    private String hospitalVerifyInfoUrl3;

    private String hospitalContactorName;

    private String hospitalContactorTelephone;

    private String hospitalAddressCode;
}
