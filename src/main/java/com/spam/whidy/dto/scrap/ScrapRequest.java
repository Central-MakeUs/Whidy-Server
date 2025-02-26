package com.spam.whidy.dto.scrap;

import com.spam.whidy.domain.scrap.Scrap;
import jakarta.validation.constraints.NotNull;

public record ScrapRequest (
        @NotNull Long placeId
){
    public Scrap toEntity(){
        return Scrap.builder()
                .placeId(placeId)
                .build();
    }

}
