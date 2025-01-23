package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.PlaceType;
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
        LocalTime businessTimeFrom,
        LocalTime businessTimeTo,
        double latitudeFrom,
        double longitudeFrom,
        double latitudeTo,
        double longitudeTo
){
}
