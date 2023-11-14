package com.instargram101.global.advice.exceptionHandler;

import com.instargram101.global.common.exception.customException.CustomException;
import com.instargram101.global.common.response.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MIN_VALUE)
public class CustomExceptionHandler {

    @ExceptionHandler(value = CustomException.class)
    public ResponseEntity<CommonApiResponse<Object>> exception(CustomException exception){
        log.error("", exception);

        //Error 내용을 담아 주고 싶을 때
/*        List<String> errors = new ArrayList<>();
        Arrays.stream(exception.getStackTrace()).forEach(error -> errors.add(error.toString()));

        return ResponseEntity
                .status(500)
                .body(CommonApiResponse.ERROR(ErrorCode.SERVER_ERROR, String.join("\n ", errors)));*/

        var status = exception.getErrorCodeInterface().getCode();
        var description = exception.getDescription();

        return ResponseEntity
                .status(status)
                .body(CommonApiResponse.ERROR(exception.getErrorCodeInterface(), description));
    }
}
