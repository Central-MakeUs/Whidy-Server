package com.spam.whidy.application.auth.exception;

import lombok.Getter;

@Getter
public class SignInFailException extends RuntimeException {

    private final String signUpCode;
    private final String message = "가입되지 않은 회원입니다.";

    public SignInFailException(String signUpCode){
        this.signUpCode = signUpCode;
    }
}
