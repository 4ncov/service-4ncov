package com.ncov.module.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("Failed to upload file.");
    }

    public FileUploadException(String msg) {
        super(msg);
    }
}
