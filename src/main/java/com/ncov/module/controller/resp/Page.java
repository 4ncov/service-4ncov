package com.ncov.module.controller.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author JackJun
 * 2019/7/4 20:11
 * Life is not just about survival.
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Page<T> {
    private List<T> data;
    private Long total;
    private Integer pageSize;
    private Integer page;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }
}
