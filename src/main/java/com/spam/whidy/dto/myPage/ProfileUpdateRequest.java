package com.spam.whidy.dto.myPage;

import jakarta.validation.constraints.NotNull;

public record ProfileUpdateRequest(
        @NotNull String name
){ }
