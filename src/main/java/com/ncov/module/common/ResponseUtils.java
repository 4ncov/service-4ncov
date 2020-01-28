package com.ncov.module.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ncov.module.controller.resp.Page;

/**
 * @author JackJun
 * 2020/01/29 01:42
 * Life is just about survival.
 */
public class ResponseUtils {

    /**
     * PageHelper page 对象转换为 简单的Response对象
     *
     * @param page pageHelper Entity
     * @param <T>  DataType
     * @return 简单的Response对象
     */
    public static <T> Page<T> pageHelperEntity2RespPage(IPage<T> page) {
        Page<T> res = new Page<>();
        res.setData(page.getRecords());
        res.setPage(page.getCurrent());
        res.setPageSize(page.getSize());
        res.setTotal(page.getTotal());
        return res;
    }
}
