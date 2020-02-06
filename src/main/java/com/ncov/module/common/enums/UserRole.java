package com.ncov.module.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserRole {

    SYSADMIN(1, "系统管理员"), SUPPLIER(2, "供应方"), HOSPITAL(3, "需求方");

    private Integer roleId;
    private String roleDescription;

    public static UserRole getRoleById(Integer id) {
        return Arrays.stream(UserRole.values())
                .collect(Collectors.toMap(UserRole::getRoleId, Function.identity()))
                .get(id);
    }
}
