package com.spam.whidy.common.exception;

import lombok.Getter;

@Getter
public class BadRequestException extends RuntimeException{

    private final int code;

    public BadRequestException(ExceptionType exception){
        super(exception.getMessage());
        code = exception.getCode();
    }

}
