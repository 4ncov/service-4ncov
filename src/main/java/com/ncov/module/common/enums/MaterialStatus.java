package com.ncov.module.common.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MaterialStatus {

    PENDING("待审核"),
    PUBLISHED("已发布"),
    PROCESSING("处理中"),
    COMPLETED("已完成");

    private String description;
}
