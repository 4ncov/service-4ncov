package com.ncov.module.controller.resp;

/**
 * @author JackJun
 * 2019/7/4 20:11
 * Life is not just about survival.
 */
public class PageResponse<T> extends RestResponse<Page<T>> {


    private PageResponse(String message, Page<T> data) {
        super(message, data);
    }

    public static <T> PageResponse<T> getResp(String message, Page<T> data) {
        return new PageResponse<>(message, data);
    }
}
