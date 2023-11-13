package com.instargram101.global.common.response;

import com.instargram101.global.common.exception.errorCode.ErrorCode;
import com.instargram101.global.common.exception.errorCode.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import java.util.Arrays;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommonApiResponse<T> {

    private Integer code; // http status 코드

    private String message;

    @Valid
    private T data;

    public static <T> CommonApiResponse<T> OK(T data) {
        var response = new CommonApiResponse<T>();
        response.code = ErrorCode.OK.getCode();
        response.message = ErrorCode.OK.getMessage();
        response.data = data;
        return response;
    }

    public static <T> CommonApiResponse<T> OK(String message, T data) {
        var response = new CommonApiResponse<T>();
        response.code = ErrorCode.OK.getCode();
        response.message = message;
        response.data = data;
        return response;
    }

    public static CommonApiResponse<Object> ERROR(ErrorCodeInterface errorCodeInterface) {
        var response = new CommonApiResponse<Object>();
        response.code = errorCodeInterface.getCode();
        return response;
    }

    public static CommonApiResponse<Object> ERROR(ErrorCodeInterface errorCodeInterface, Throwable tx) {
        var response = new CommonApiResponse<Object>();
        response.code = errorCodeInterface.getCode();
        response.message = errorCodeInterface.getMessage();
        response.data = Arrays.toString(tx.getStackTrace());
        return response;
    }

    public static CommonApiResponse<Object> ERROR(ErrorCodeInterface errorCodeInterface, String description){
        var response = new CommonApiResponse<Object>();
        response.code = errorCodeInterface.getCode();
        response.message = errorCodeInterface.getMessage();
        response.data = description;
        return response;
    }
}