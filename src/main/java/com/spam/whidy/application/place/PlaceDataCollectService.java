package com.spam.whidy.application.place;

import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceDataCollector;
import com.spam.whidy.domain.place.PlaceRepository;
import com.spam.whidy.domain.place.PlaceType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
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
            placeRepository.saveAll(places);
        }
    }

}
