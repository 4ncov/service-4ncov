package com.ncov.module.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserStatus {

    PENDING("未认证"), VERIFIED("已认证"), SUSPENDED("已注销");

    private String description;
}
