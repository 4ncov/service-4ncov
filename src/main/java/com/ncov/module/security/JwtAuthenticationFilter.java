package com.ncov.module.security;

import com.ncov.module.common.Constants;
import io.jsonwebtoken.ExpiredJwtException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.inject.Inject;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author JackJun
 * 2019/6/27 18:39
 * Life is not just about survival.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String SET_TOKEN = "set-Token";
    @Inject
    private JwtTokenProvider jwtTokenProvider;
    @Inject
    private Environment env;

    /**
     * 设置来宾用户或者授权用户
     * 注意：禁止在来宾用户的context中存储信息、
     *
     * @param httpServletRequest  request
     * @param httpServletResponse response
     * @param filterChain         filter
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest httpServletRequest, @NotNull HttpServletResponse httpServletResponse, @NotNull FilterChain filterChain) throws ServletException, IOException {
//        if (shouldPassSwaggerUrl(httpServletRequest)) {
//            logger.info("Swagger request allowed {}", httpServletRequest.getRequestURI());
//        } else {
//            String jwt = getJwtFormRequest(httpServletRequest);
//
//            try {
//                if (StringUtils.hasText(jwt) && jwtTokenProvider.validateToken(jwt)) {
//                    Authentication authentication = this.jwtTokenProvider.getAuthentication(jwt);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                } else {
//                    Authentication authentication = new AnonymousAuthenticationToken("anonymous",
//                            new User("guest", "guest", Collections.EMPTY_LIST),
//                            Collections.EMPTY_LIST);
//                    String token = this.jwtTokenProvider.generateToken(authentication);
//                    httpServletResponse.setHeader(SET_TOKEN, token);
//                    SecurityContextHolder.getContext().setAuthentication(authentication);
//                }
//            } catch (ExpiredJwtException ex) {
//                logger.info("Security exception for user {} - {}", ex.getClaims().getSubject(), ex.getMessage());
//                httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            }
//        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String getJwtFormRequest(HttpServletRequest request) {
        return request.getHeader("Authorization");
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
}
