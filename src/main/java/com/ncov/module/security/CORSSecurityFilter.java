package com.ncov.module.security;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author JackJun
 * 2020/01/29 01:37
 * Life is not just about survival.
 */
@Component
public class CORSSecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) servletResponse;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "1728000");
        res.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache,X_Requested_With, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization");
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("XDomainRequestAllowed", "1");
        // 如果是OPTIONS则结束请求
        if (HttpMethod.OPTIONS.toString().equals(((HttpServletRequest) servletRequest).getMethod())) {
            ((HttpServletResponse) servletResponse).setStatus(HttpStatus.NO_CONTENT.value());
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);

    }
}
