package com.spam.whidy.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {

    USER_NOT_VALID("유효하지 않은 사용자입니다.", 401),
    SIGN_UP_CODE_NOT_VALID("회원가입 코드가 유효하지 않습니다.", 401),
    STATE_NOT_VALID("Oauth 인증을 위한 state 가 유효하지 않습니다.", 401),
    TOKEN_NOT_EXIST("요청에 토큰이 포함되지 않았습니다.", 401),
    TOKEN_NOT_VALID("토큰이 유효하지 않습니다.", 401),
    DUPLICATED_USER("이미 가입된 회원입니다.", 409);

    private final String message;
    private final int code;

    ExceptionType(String message, int code){
        this.message = message;
        this.code = code;
    }
}
