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
    private long total;
    private long pageSize;
    private long page;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPage() {
        return page;
    }

    public void setPage(long page) {
        this.page = page;
    }
}
