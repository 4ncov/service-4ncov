package com.ncov.module.common.exception;

import com.ncov.module.controller.resp.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

/**
 * @author JackJun
 * 2019/7/3 11:52
 * Life is not just about survival.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse formValidateFailed(MissingServletRequestParameterException e) {
        logger.error("ParameterError:{}", e.getMessage());
        return RestResponse.getResp("参数错误!");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse formValidateFailed(MethodArgumentNotValidException e) {
        logger.error(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestResponse loginFailure(BadCredentialsException e) {
        return RestResponse.getResp("账号或密码错误！");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> globalException(Exception e) {
        ByteArrayOutputStream exceptionStream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(exceptionStream));
        String exceptionMsg = exceptionStream.toString();
        logger.error(exceptionMsg);
        logger.error(e.getClass().getName());
        //TODO 全局异常记录
        return new ResponseEntity<>(RestResponse.getResp("内部程序错误！请联系管理员！"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 处理自定义异常
     */
    @ExceptionHandler(RRException.class)
    public ResponseEntity<?> handleRRException(RRException e){
        //TODO 全局异常记录
        ByteArrayOutputStream exceptionStream = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(exceptionStream));
        String exceptionMsg =e.getMsg();
        return new ResponseEntity<>(RestResponse.getResp(exceptionMsg), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
