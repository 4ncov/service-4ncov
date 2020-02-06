package com.ncov.module.security;

import com.ncov.module.common.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class JwtUser {

    private Long id;
    private String userNickName;
    private UserRole userRole;
    private Long organisationId;
    private String organisationName;
}
