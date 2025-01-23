package com.spam.whidy.application.place;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.repository.PlaceRepository;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.dto.place.CafeDTO;
import com.spam.whidy.dto.place.FreeSpaceDTO;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlaceService {

    private final PlaceRepository placeRepository;

    @Transactional
    public Long save(Place place){
        placeRepository.save(place);
        return place.getId();
    }

    public Place findById(Long id){
        return placeRepository.findById(id)
                .orElseThrow(() -> new BadRequestException(ExceptionType.PLACE_NOT_FOUND));
    }

    public List<PlaceDTO> searchByCondition(PlaceSearchCondition condition) {
        return placeRepository.searchByCondition(condition);
    }

    public CafeDTO findCafe(PlaceType placeType, Long id) {
        Place place = placeRepository.findByPlaceTypeAndId(placeType, id)
                .orElseThrow(() -> new BadRequestException(ExceptionType.PLACE_NOT_FOUND));
        return CafeDTO.of(place);
    }

    public FreeSpaceDTO findFreeSpace(PlaceType placeType, Long id) {
        Place place = placeRepository.findByPlaceTypeAndId(placeType, id)
                .orElseThrow(() -> new BadRequestException(ExceptionType.PLACE_NOT_FOUND));
        return FreeSpaceDTO.of(place);
    }

}

