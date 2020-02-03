package com.ncov.module.controller.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ncov.module.common.ServiceExecuteResult;
import lombok.Builder;

/**
 * @author JackJun
 * 2019/7/4 20:01
 * Life is not just about survival.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResponse<T> {
    private String message;
    private T data;

    /* Static */

    @Builder
    RestResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    private RestResponse(String message) {
        this.message = message;
    }

    public static <T> RestResponse<T> getResp(String message, T data) {
        return new RestResponse<>(message, data);
    }

    /* Constructor */

    public static <T> RestResponse<T> getResp(String message) {
        return new RestResponse<>(message);
    }

    public static <T> RestResponse<T> getResp(ServiceExecuteResult<T> result) {
        return new RestResponse<>(result.getMessage(), result.getData());
    }

    /* Getter And Setter */

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
