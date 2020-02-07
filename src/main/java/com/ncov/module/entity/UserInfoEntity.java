package com.ncov.module.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.ncov.module.common.enums.UserRole;
import lombok.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

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
    private String userIdentificationNumber;

    public boolean isHospital() {
        return UserRole.HOSPITAL.getRoleId().equals(getUserRoleId());
    }

    public boolean isSupplier() {
        return UserRole.SUPPLIER.getRoleId().equals(getUserRoleId());
    }

    public boolean isAbleToResetPassword(String userPhone, String userIdentificationNumber) {
        return StringUtils.equals(userPhone, getUserPhone())
                && StringUtils.equals(userIdentificationNumber, getUserIdentificationNumber());
    }

    public void changePassword(String password) {
        setUserPasswordSHA256(DigestUtils.sha256Hex(password));
    }
}
