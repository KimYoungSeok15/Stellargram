package com.instargram101.global.common.exception.errorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode implements ErrorCodeInterface{
    OK(200, "OK"),
    NULL_POINT(500, "NULL POINT EXCEPTION"),
    SERVER_ERROR(500, "SERVER ERROR");

    private final Integer code;

    private final String message;

}
