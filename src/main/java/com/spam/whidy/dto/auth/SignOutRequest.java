package com.spam.whidy.dto.auth;

import jakarta.validation.constraints.NotNull;

public record SignOutRequest (
        @NotNull String accessToken,
        @NotNull String refreshToken
){ }
