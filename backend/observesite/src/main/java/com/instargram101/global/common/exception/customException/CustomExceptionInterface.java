package com.instargram101.global.common.exception.customException;

import com.instargram101.global.common.exception.errorCode.ErrorCodeInterface;

public interface CustomExceptionInterface {
    ErrorCodeInterface getErrorCodeInterface();

    String getDescription();
}

