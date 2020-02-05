package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TableName("user_info")
public class UserInfoEntity {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String userNickName;
    private String userSalt;
    @TableField(value = "user_password_SHA256")
    private String userPasswordSHA256;
    private Date gmtCreated;
    private Date gmtModified;
    private String userPhone;
    private Integer userRoleId;
}
