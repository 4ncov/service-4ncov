package com.ncov.module.security;

import com.ncov.module.common.Constants;
import com.ncov.module.common.enums.UserRole;
import com.ncov.module.config.AuthorisationEndpointConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.env.Environment;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Collections.singletonList;

/**
 * @author JackJun
 * 2019/6/27 18:39
 * Life is not just about survival.
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Inject
    private Environment env;
    @Inject
    private AuthorisationEndpointConfiguration authorisationEndpointConfiguration;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
        log.debug("Request uri = [{}], method = [{}]",
                httpServletRequest.getRequestURI(), httpServletRequest.getMethod());

        if (shouldPassSwaggerUrl(httpServletRequest)) {
            log.debug("Swagger url allowed, uri=[{}]", httpServletRequest.getRequestURI());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        if (shouldIgnoreAuthorisation(httpServletRequest)) {
            log.debug("Request bypasses authorisation, uri=[{}], method=[{}]",
                    httpServletRequest.getRequestURI(), httpServletRequest.getMethod());
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String jwt = getJwtFormRequest(httpServletRequest);
        try {
            Claims jwtClaims = Jwts.parser().setSigningKey(authorisationEndpointConfiguration.getJwtSecret())
                    .parseClaimsJws(jwt).getBody();
            JwtUser jwtUser = JwtUser.builder()
                    .id(jwtClaims.get("id", Long.class))
                    .userNickName(jwtClaims.get("userNickName", String.class))
                    .userRole(UserRole.valueOf(jwtClaims.get("userRole", String.class)))
                    .organisationId(jwtClaims.get("organisationId", Long.class))
                    .organisationName(jwtClaims.get("organisationName", String.class))
                    .build();
            SecurityContextHolder.getContext().setAuthentication(new PreAuthenticatedAuthenticationToken(
                    "X-JWT-TOKEN", jwtUser,
                    singletonList(new SimpleGrantedAuthority("ROLE_" + jwtUser.getUserRole().name()))));
            log.debug("Jwt user set to security context holder, userId=[{}]", jwtUser.getId());
        } catch (Exception ex) {
            log.warn("Token validation failed", ex);
            httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFormRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Authorization"))
                .map(header -> header.replace("Bearer ", ""))
                .orElse("");
    }

    private boolean shouldPassSwaggerUrl(HttpServletRequest request) {
        String uri = request.getRequestURI();
        return ("/swagger-ui.html".equals(uri)
                || uri.startsWith("/webjars")
                || uri.startsWith("/swagger-resources")
                || uri.startsWith("/v2/api-docs"))
                && Arrays.asList(Constants.SPRING_PROFILE_LOCAL, Constants.SPRING_PROFILE_DEVELOPMENT)
                .contains(env.getActiveProfiles()[0]);
    }

    private boolean shouldIgnoreAuthorisation(HttpServletRequest request) {
        return authorisationEndpointConfiguration.getEndpointsIgnoreToken().stream()
                .anyMatch(endpoint -> endpoint.getPath().equals(request.getRequestURI())
                        && endpoint.getMethods().contains(request.getMethod()));
    }
}
