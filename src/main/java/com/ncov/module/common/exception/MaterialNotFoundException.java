package com.ncov.module.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class MaterialNotFoundException extends RuntimeException {

    public MaterialNotFoundException() {
        super("Material info does not exist");
    }
}
