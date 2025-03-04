package com.spam.whidy.dto.place;

import lombok.Builder;

@Builder
public record PlaceRequestSearchCondition (
        String name,
        String address,
        Boolean processed
){ }
