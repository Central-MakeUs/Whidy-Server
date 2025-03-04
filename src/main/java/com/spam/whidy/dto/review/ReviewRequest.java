package com.spam.whidy.dto.review;

import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.review.*;
import com.spam.whidy.domain.user.User;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record ReviewRequest(
        Long reviewId,
        @NotNull Long placeId,
        @NotNull Float score,
        @NotNull Set<ReviewKeyword> keywords,
        Boolean isReserved,
        WaitTime waitTime,
        Set<VisitPurpose> visitPurposes,
        CompanionType companionType,
        String comment
) {
    public Review toEntity(Place place, User user) {
        return Review.builder()
                .place(place)
                .score(score)
                .keywords(keywords)
                .isReserved(isReserved)
                .waitTime(waitTime)
                .visitPurposes(visitPurposes)
                .companionType(companionType)
                .createUser(user)
                .comment(comment)
                .build();
    }

    public Review toUpdateEntity() {
        return Review.builder()
                .score(score)
                .keywords(keywords)
                .isReserved(isReserved)
                .waitTime(waitTime)
                .visitPurposes(visitPurposes)
                .companionType(companionType)
                .comment(comment)
                .build();
    }
}
