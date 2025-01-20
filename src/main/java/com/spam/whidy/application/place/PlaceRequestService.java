package com.spam.whidy.application.place;

import com.spam.whidy.domain.place.placeRequest.PlaceRequest;
import com.spam.whidy.domain.place.placeRequest.PlaceRequestRepository;
import com.spam.whidy.dto.place.PlaceRequestDTO;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PlaceRequestService {

    private final PlaceRequestRepository placeRequestRepository;

    @Transactional
    public Long save(PlaceRequestDTO requestDTO){
        PlaceRequest request = requestDTO.toEntity();
        placeRequestRepository.save(request);
        return request.getId();
    }

    public List<PlaceRequest> searchByCondition(PlaceRequestSearchCondition condition) {
        return placeRequestRepository.searchByCondition(condition);
    }

}
