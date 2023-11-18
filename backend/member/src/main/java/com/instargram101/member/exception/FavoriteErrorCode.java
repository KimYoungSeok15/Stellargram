package com.instargram101.member.exception;

import com.instargram101.global.common.exception.errorCode.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum FavoriteErrorCode implements ErrorCodeInterface {

    MEMBER_ALREADY_LIKES_STAR(400, "member already likes star"),
    MEMBER_ALREADY_DISLIKES_STAR(400, "member already dislikes star"),
    ;

    private final Integer code;

    private final String message;

}
