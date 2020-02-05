package com.ncov.module.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class DuplicateException extends RuntimeException {

    public DuplicateException(String msg) {
        super(msg);
    }
}
