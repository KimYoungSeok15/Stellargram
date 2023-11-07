package com.instargram101.global.advice.exceptionHandler;

import com.instargram101.global.common.exception.errorCode.ErrorCode;
import com.instargram101.global.common.response.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonApiResponse<Object>> exception(HttpMessageNotReadableException exception) {
        log.error("값이 없는 요청", exception);
        return ResponseEntity
                .status(400)
                .body(CommonApiResponse.ERROR(ErrorCode.INVALID_REQUEST));
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<CommonApiResponse<Object>> exception(MissingRequestHeaderException exception) {
        log.error("헤더 값 없음", exception);
        return ResponseEntity
                .status(400)
                .body(CommonApiResponse.ERROR(ErrorCode.MISSING_HEADER));
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<CommonApiResponse<Object>> exception(Exception exception){
        log.error("", exception);

        //Error 내용을 담아 주고 싶을 때
/*        List<String> errors = new ArrayList<>();
        Arrays.stream(exception.getStackTrace()).forEach(error -> errors.add(error.toString()));

        return ResponseEntity
                .status(500)
                .body(CommonApiResponse.ERROR(ErrorCode.SERVER_ERROR, String.join("\n ", errors)));*/
        return ResponseEntity
                .status(500)
                .body(CommonApiResponse.ERROR(ErrorCode.SERVER_ERROR));
    }
}
