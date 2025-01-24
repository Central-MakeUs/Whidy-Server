package com.spam.whidy.application.place;

import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceDataCollector;
import com.spam.whidy.domain.place.repository.PlaceRepository;
import com.spam.whidy.domain.place.PlaceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PlaceDataCollectService {

    private final PlaceRepository placeRepository;
    private final Map<PlaceType, PlaceDataCollector> placeDataCollector;

    public PlaceDataCollectService(PlaceRepository placeRepository, List<PlaceDataCollector> placeDataCollectors){
        this.placeRepository = placeRepository;
        this.placeDataCollector = placeDataCollectors.stream().collect(Collectors.toMap(PlaceDataCollector::dataType, v -> v));
    }

    public void collectAll(){
        for(PlaceDataCollector collector : placeDataCollector.values()){
            List<Place> places = collector.collect();
            for(Place place : places){
                try {
                    placeRepository.save(place);
                }catch (DataIntegrityViolationException e){
                    log.error("수집된 장소 데이터 저장 중 에러 발생 : {}", e.getMessage());
                }
            }
        }
    }

}
