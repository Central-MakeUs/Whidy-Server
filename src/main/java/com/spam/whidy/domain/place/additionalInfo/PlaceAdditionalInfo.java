package com.spam.whidy.domain.place.additionalInfo;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.spam.whidy.domain.place.additionalInfo.cafe.CafeAdditionalInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CafeAdditionalInfo.class),
        @JsonSubTypes.Type(value = FreeSpaceAdditionalInfo.class),
})
public interface PlaceAdditionalInfo {
}
