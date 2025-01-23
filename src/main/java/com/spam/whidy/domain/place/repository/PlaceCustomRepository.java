package com.spam.whidy.domain.place.repository;

import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;

import java.util.List;

public interface PlaceCustomRepository {
    List<PlaceDTO> searchByCondition(PlaceSearchCondition condition);
}
