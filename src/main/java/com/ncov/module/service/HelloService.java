package com.ncov.module.service;

import com.ncov.module.common.ServiceExecuteResult;
import org.springframework.stereotype.Service;

/**
 * @author JackJun
 * @date 2020/1/29 1:49 上午
 */
@Service
public class HelloService {

    public ServiceExecuteResult<?> hello() {
        return ServiceExecuteResult.createSuccessResult("success");
    }
}
