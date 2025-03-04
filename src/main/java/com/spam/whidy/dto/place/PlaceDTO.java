package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.PlaceType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlaceDTO {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Integer beveragePrice;
    private Integer reviewNum;
    private Float reviewScore;
    private PlaceType placeType;
    private String thumbnail;

}