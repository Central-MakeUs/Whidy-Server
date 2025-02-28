package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.BusinessHour;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.domain.place.additionalInfo.cafe.CafeAdditionalInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CafeDTO {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Integer beveragePrice;
    private Integer reviewNum;
    private Float reviewScore;
    private PlaceType placeType;
    private CafeAdditionalInfo additionalInfo;
    private Set<BusinessHour> businessHours;
    private List<String> images;

    public static CafeDTO of(Place place){
        CafeDTO cafeDTO = new CafeDTO();
        cafeDTO.id = place.getId();
        cafeDTO.name = place.getName();
        cafeDTO.address = place.getAddress();
        cafeDTO.latitude = place.getLatitude();
        cafeDTO.longitude = place.getLongitude();
        cafeDTO.beveragePrice = place.getBeveragePrice();
        cafeDTO.reviewNum = place.getReviewNum();
        cafeDTO.reviewScore = place.getReviewScore();
        cafeDTO.placeType = place.getPlaceType();
        cafeDTO.additionalInfo = (CafeAdditionalInfo) place.getAdditionalInfo();
        cafeDTO.businessHours = place.getBusinessHours();
        cafeDTO.images = place.getImages();
        return cafeDTO;
    }
}
