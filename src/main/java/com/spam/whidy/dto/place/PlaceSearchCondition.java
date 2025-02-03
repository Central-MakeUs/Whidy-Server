package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.PlaceType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Builder
public record PlaceSearchCondition (
        Integer reviewScoreFrom,
        Integer reviewScoreTo,
        Integer beverageFrom,
        Integer beverageTo,
        Set<PlaceType> placeType,
        Set<DayOfWeek> businessDayOfWeek,
        LocalTime visitTimeFrom,
        LocalTime visitTimeTo,
        @NotNull Double centerLatitude,
        @NotNull Double centerLongitude,
        @NotNull Double radius
){
}
