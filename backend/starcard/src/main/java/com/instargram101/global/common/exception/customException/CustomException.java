package com.instargram101.global.common.exception.customException;

import com.instargram101.global.common.exception.errorCode.ErrorCodeInterface;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException implements CustomExceptionInterface{

    private final ErrorCodeInterface errorCodeInterface;

    private final String description;

    public CustomException(ErrorCodeInterface errorCodeInterface){
        super(errorCodeInterface.getMessage());
        this.errorCodeInterface = errorCodeInterface;
        this.description = errorCodeInterface.getMessage();
    }

    public CustomException(ErrorCodeInterface errorCodeInterface, String description){
        super(description);
        this.errorCodeInterface = errorCodeInterface;
        this.description = description;
    }

    public CustomException(ErrorCodeInterface errorCodeInterface, Throwable tx){
        super(tx);
        this.errorCodeInterface = errorCodeInterface;
        this.description = errorCodeInterface.getMessage();
    }

    public CustomException(ErrorCodeInterface errorCodeInterface, Throwable tx, String description){
        super(tx);
        this.errorCodeInterface = errorCodeInterface;
        this.description = description;
    }
}
