package com.spam.whidy.application.place;

import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import com.spam.whidy.domain.place.placeRequest.PlaceRequestRepository;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceRequestService {

    private final PlaceRequestRepository placeRequestRepository;

    public List<PlaceRequest> searchByCondition(PlaceRequestSearchCondition condition) {
        return placeRequestRepository.searchByCondition(condition);
    }

}
