package com.ncov.module.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserStatus {

    PENDING("待审核"), ACTIVE("使用中"), SUSPENDED("已注销");

    private String description;
}
