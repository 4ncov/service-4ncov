package com.ncov.module.security;

import com.ncov.module.common.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public Long getUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getId();
    }

    public String getUserNickName() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getUserNickName();
    }

    public UserRole getUserRole() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getUserRole();
    }
}
