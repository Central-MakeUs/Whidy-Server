package com.spam.whidy.domain.auth;

import com.spam.whidy.domain.auth.oauth.OAuthType;

public record SignUpInfo(
        OAuthType oauthType,
        String oauthId
) {
}
