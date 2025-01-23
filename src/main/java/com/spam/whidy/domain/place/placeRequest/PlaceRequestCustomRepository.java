package com.spam.whidy.domain.place.placeRequest;

import com.spam.whidy.dto.place.PlaceRequestSearchCondition;

import java.util.List;

public interface PlaceRequestCustomRepository {

    List<PlaceRequest> searchByCondition(PlaceRequestSearchCondition condition);
    List<PlaceRequest> searchByUserAndCondition(Long userId, PlaceRequestSearchCondition condition);
}
