package com.spam.whidy.dto.auth;

import jakarta.validation.constraints.NotNull;

public record SignUpRequest(
        @NotNull String signUpCode,
        @NotNull String email,
        @NotNull String name
) {
}
