package com.spam.whidy.dto.place;


import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import jakarta.validation.constraints.NotNull;

// 사용자가 장소 등록 요청시 사용되는 dto
public record PlaceRequestDTO (
        @NotNull String address,
        @NotNull String name,
        @NotNull Double latitude,
        @NotNull Double longitude
) {
    public PlaceRequest toEntity() {
        return PlaceRequest.builder()
                .address(address)
                .name(name)
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}
