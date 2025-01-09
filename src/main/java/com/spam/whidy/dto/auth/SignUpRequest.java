package com.spam.whidy.dto.auth;

public record SignUpRequest(
        String signUpCode,
        String email,
        String name
) {
}
