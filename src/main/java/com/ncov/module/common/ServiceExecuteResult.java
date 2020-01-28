package com.ncov.module.common;

import org.springframework.http.HttpStatus;

/**
 * @author JackJun
 * 09/09/2019 18:29
 * Life is just about survival.
 */
public class ServiceExecuteResult<T> {
    private String message;
    private boolean success = true;
    private T data;

    private ServiceExecuteResult(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private ServiceExecuteResult(String message, boolean success, T data) {
        this.message = message;
        this.success = success;
        this.data = data;
    }

    public ServiceExecuteResult() {

    }

    public static <T> ServiceExecuteResult createSuccessResult(String message, T data) {
        return new ServiceExecuteResult<>(message, data);
    }

    public static ServiceExecuteResult createSuccessResult(String message) {
        return new ServiceExecuteResult<>(message, null);
    }

    public static <T> ServiceExecuteResult createFailResult(String message, T data) {
        return new ServiceExecuteResult<>(message, false, data);
    }

    public static ServiceExecuteResult createFailResult(String message) {
        return new ServiceExecuteResult<>(message, false, null);
    }

    public static ServiceExecuteResult createCustomResult(boolean success, String message) {
        return new ServiceExecuteResult<>(message, success, null);
    }

    public HttpStatus getHttpStatusCode() {
        return success ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
