package com.spam.whidy.dto.review;

import jakarta.validation.constraints.NotNull;

public record ReviewSearchCondition (
        @NotNull Long placeId,
        Integer offset,
        Integer limit
){ }
