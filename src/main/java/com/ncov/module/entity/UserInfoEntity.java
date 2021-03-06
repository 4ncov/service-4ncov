package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.common.enums.UserStatus;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;

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
    private Short deleted;
    private String status;

    public boolean isHospital() {
        return UserRole.HOSPITAL.getRoleId().equals(getUserRoleId());
    }

    public boolean isSupplier() {
        return UserRole.SUPPLIER.getRoleId().equals(getUserRoleId());
    }

    public void changePassword(String password) {
        setUserPasswordSHA256(DigestUtils.sha256Hex(password));
    }

    public void verify() {
        setStatus(UserStatus.VERIFIED.name());
        setGmtModified(new Date());
    }

    public boolean isVerified() {
        return UserStatus.VERIFIED.name().equals(getStatus());
    }
}
