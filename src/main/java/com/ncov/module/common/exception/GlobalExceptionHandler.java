package com.ncov.module.common.exception;

import com.ncov.module.controller.resp.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Objects;
import java.util.Optional;

/**
 * @author JackJun
 * 2019/7/3 11:52
 * Life is not just about survival.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse formValidateFailed(MissingServletRequestParameterException e) {
        log.error("ParameterError:{}", e.getMessage());
        return RestResponse.getResp("参数错误!");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse formValidateFailed(MethodArgumentNotValidException e) {
        log.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
        return RestResponse.getResp(e.getBindingResult().getFieldError().getDefaultMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public RestResponse processMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return RestResponse.getResp(e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public RestResponse loginFailure(BadCredentialsException e) {
        return RestResponse.getResp("账号或密码错误！");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestResponse processAccessDeniedException(AccessDeniedException e) {
        return RestResponse.getResp("当前帐号无权访问.");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalException(Exception e) {
        ResponseStatus errorHttpStatus = e.getClass().getAnnotation(ResponseStatus.class);
        if (Objects.nonNull(errorHttpStatus)) {
            log.warn("Managed exception, http status {}", errorHttpStatus.code(), e);
            return new ResponseEntity<>(RestResponse.getResp(e.getMessage()), errorHttpStatus.code());
        }
        log.error("System error", e);
        return new ResponseEntity<>(RestResponse.getResp("内部程序错误！请联系管理员！"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
