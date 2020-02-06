package com.ncov.module.security;

import com.ncov.module.common.enums.UserRole;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class UserContext {

    public Long getUserId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getId();
    }

    public UserRole getUserRole() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getUserRole();
    }

    public Long getOrganisationId() {
        return ((JwtUser) SecurityContextHolder.getContext().getAuthentication().getCredentials()).getOrganisationId();
    }
}
