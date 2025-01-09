package com.spam.whidy.dto.auth;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import jakarta.validation.constraints.NotNull;

public record SignInRequest(
        @NotNull
        OAuthType oauthType,
        @NotNull
        String code,
        @NotNull
        String state
) {
}
