package com.spam.whidy.dto.auth;

import com.spam.whidy.domain.auth.AuthToken;

public record SignInResponse (
        AuthToken authToken,
        Long userId
){ }
