package com.spam.whidy.domain.place;

import com.spam.whidy.domain.place.additionalInfo.*;
import com.spam.whidy.domain.place.additionalInfo.cafe.CafeAdditionalInfo;
import lombok.Getter;

@Getter
public enum PlaceType {

    STUDY_CAFE(CafeAdditionalInfo.class),
    GENERAL_CAFE(CafeAdditionalInfo.class),
    FRANCHISE_CAFE(CafeAdditionalInfo.class),
    FREE_PICTURE(FreeSpaceAdditionalInfo.class),
    FREE_STUDY_SPACE(FreeSpaceAdditionalInfo.class),
    FREE_CLOTHES_RENTAL(FreeSpaceAdditionalInfo.class);

    private final Class<? extends PlaceAdditionalInfo> additionalInfoClass;

    PlaceType(Class<? extends PlaceAdditionalInfo> additionalInfoClass) {
        this.additionalInfoClass = additionalInfoClass;
    }

}
