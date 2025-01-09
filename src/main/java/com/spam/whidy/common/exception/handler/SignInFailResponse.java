package com.spam.whidy.common.exception.handler;

public record SignInFailResponse (
        String message,
        String signUpCode
) { }
