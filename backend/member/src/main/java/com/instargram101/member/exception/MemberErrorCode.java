package com.instargram101.member.exception;

import com.instargram101.global.common.exception.errorCode.ErrorCodeInterface;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 여기다 Member에 관련된 Custom Error를 작성하면 됩니다.
 *
 * <p> 이 ErrorCode를 사용하는 방법은 CustomException에다 해당 에러코드를 담아 throw 하시면 됩니다.
 *
 * <p> Ex) throw new CustomException(MemberErrorCode.Member_Not_Found);
 *
 * @author S.H.Kim
* @param {code} Http 에러 코드
 * @param {message} 안의 description
* @type {(code: Integer, message: String) => enum}
*/


@Getter
@AllArgsConstructor
public enum MemberErrorCode implements ErrorCodeInterface {

    Member_Not_Found(404, "User Not Found."),
    Invalid_User(400, "Invalid User")
    //TODO: 추가로 여기다 채워넣으면 됩니다.
    ;

    private final Integer code;

    private final String message;

}
