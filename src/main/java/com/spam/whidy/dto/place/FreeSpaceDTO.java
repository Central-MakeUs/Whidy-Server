package com.spam.whidy.dto.place;

import com.spam.whidy.domain.place.BusinessHour;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.domain.place.additionalInfo.FreeSpaceAdditionalInfo;
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
public class FreeSpaceDTO {

    private Long id;
    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private Integer beveragePrice;
    private Integer reviewNum;
    private Float reviewScore;
    private PlaceType placeType;
    private FreeSpaceAdditionalInfo additionalInfo;
    private Set<BusinessHour> businessHours;
    private List<String> images;

    public static FreeSpaceDTO of(Place place){
        FreeSpaceDTO freeSpaceDTO = new FreeSpaceDTO();
        freeSpaceDTO.id = place.getId();
        freeSpaceDTO.name = place.getName();
        freeSpaceDTO.address = place.getAddress();
        freeSpaceDTO.latitude = place.getLatitude();
        freeSpaceDTO.longitude = place.getLongitude();
        freeSpaceDTO.beveragePrice = place.getBeveragePrice();
        freeSpaceDTO.reviewNum = place.getReviewNum();
        freeSpaceDTO.reviewScore = place.getReviewScore();
        freeSpaceDTO.placeType = place.getPlaceType();
        freeSpaceDTO.additionalInfo = (FreeSpaceAdditionalInfo) place.getAdditionalInfo();
        freeSpaceDTO.businessHours = place.getBusinessHours();
        freeSpaceDTO.images = place.getImages();
        return freeSpaceDTO;
    }
}
