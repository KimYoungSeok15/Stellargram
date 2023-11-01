package com.instargram101.global.advice.exceptionHandler;

import com.instargram101.global.common.exception.errorCode.ErrorCode;
import com.instargram101.global.common.response.CommonApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
@Order(value = Integer.MAX_VALUE)
public class GlobalExceptionHandler {
    /**
     * 잘못된 요청 처리
     */
    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<CommonApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException e){

        public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e){
        log.error("잘못된 요청 처리됨", e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
//                .body(CommonApiResponse.ERROR(HttpStatus.BAD_REQUEST,e.getMessage()));
                        .body(e.getMessage());

    }

    /**
     * 널포인터 처리
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<CommonApiResponse<Object>> handleNullPointerException(NullPointerException e){
        log.error("Null 에러 처리됨",e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonApiResponse.ERROR(HttpStatus.INTERNAL_SERVER_ERROR,e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonApiResponse<Object>> exception(Exception exception){
        log.error("", exception);
        log.error("핸들러 테스트");

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
