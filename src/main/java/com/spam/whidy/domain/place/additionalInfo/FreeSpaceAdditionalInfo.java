package com.spam.whidy.domain.place.additionalInfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreeSpaceAdditionalInfo implements PlaceAdditionalInfo{
    private String seatingTypes;
    private String amenities;
    private String amenitiesBasic;
    private String description;
    private Boolean reservationRequired;
    private String contact;
    private String homepage;
}
