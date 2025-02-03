package com.spam.whidy.common.exception;

import lombok.Getter;

@Getter
public enum ExceptionType {

    // common
    BAD_REQUEST("잘못된 요청입니다.", 400),
    UNAUTHORIZED("로그인이 필요합니다.", 401),
    FORBIDDEN("권한이 없습니다.", 403),

    // 사용자 관련
    USER_NOT_FOUND("유효하지 않은 사용자입니다.", 404),
    SIGN_UP_CODE_NOT_VALID("회원가입 코드가 유효하지 않습니다.", 401),
    STATE_NOT_VALID("Oauth 인증을 위한 state 가 유효하지 않습니다.", 401),
    TOKEN_NOT_EXIST("요청에 토큰이 포함되지 않았습니다.", 401),
    TOKEN_NOT_VALID("토큰이 유효하지 않습니다.", 401),
    DUPLICATED_USER("이미 가입된 회원입니다.", 409),
    DUPLICATED_EMAIL_USER("이미 가입된 이메일입니다.", 409),

    // 장소 관련
    PLACE_NOT_FOUND("존재하지 않는 장소입니다.", 404),
    REVIEW_NOT_FOUND("존재하지 않는 리뷰입니다.", 404),
    REVIEW_CONFLICT("해당 장소에 대한 리뷰를 이미 작성하였습니다.", 409),
    SCRAP_NOT_FOUND("스크랩을 찾을 수 없습니다.", 404),
    SCRAP_CONFLICT("해당 장소에 대한 스크랩이 이미 존재합니다.", 409),
    DAY_OF_WEEK_NOT_VALID("요일 입력이 올바르지 않습니다.", 400),
    PLACE_TYPE_NOT_VALID("장소타입 입력이 올바르지 않습니다.", 400);

    private final String message;
    private final int code;

    ExceptionType(String message, int code){
        this.message = message;
        this.code = code;
    }
}
