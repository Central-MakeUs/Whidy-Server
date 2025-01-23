package com.spam.whidy.dto.place;

import com.google.gson.Gson;
import com.querydsl.core.annotations.QueryProjection;
import com.spam.whidy.domain.place.BusinessHour;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import lombok.*;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlaceDTO {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Integer beveragePrice;
    private Float reviewScore;
    private PlaceType placeType;

    public static PlaceDTO of(Place place){
        return PlaceDTO.builder()
                .id(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .latitude(place.getLatitude())
                .longitude(place.getLongitude())
                .beveragePrice(place.getBeveragePrice())
                .reviewScore(place.getReviewScore())
                .placeType(place.getPlaceType())
//                .additionalInfo(gson.toJson(place.getAdditionalInfo()))
                .build();
    }
}
