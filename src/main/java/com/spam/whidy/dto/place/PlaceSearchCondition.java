package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.PlaceType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

@Getter
@AllArgsConstructor
public class PlaceSearchCondition {

    private Integer reviewScoreFrom;
    private Integer reviewScoreTo;
    private Integer beverageFrom;
    private Integer beverageTo;
    private Set<PlaceType> type;
    private Set<DayOfWeek> businessDayOfWeek;
    private LocalTime businessTimeFrom;
    private LocalTime businessTimeTo;
    private double latitudeFrom; // 남서쪽 위도
    private double longitudeFrom; // 남서쪽 경도
    private double latitudeTo; // 북동쪽 위도
    private double longitudeTo; // 북동쪽 경도
}
