package com.spam.whidy.api.place;

import com.spam.whidy.application.place.PlaceDataCollectService;
import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.domain.place.Place;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.dto.place.CafeDTO;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {

    private final PlaceService placeService;
    private final PlaceDataCollectService placeDataCollector;

    @GetMapping("/search")
    public List<PlaceDTO> searchByCondition(PlaceSearchCondition condition){
        return placeService.searchByCondition(condition);
    }

    @GetMapping("/general-cafe/{id}")
    public CafeDTO findCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.GENERAL_CAFE, id);
    }

    @GetMapping("/franchise-cafe/{id}")
    public CafeDTO findFranchiseCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.FRANCHISE_CAFE, id);
    }

    @GetMapping("/study-cafe/{id}")
    public CafeDTO findStudyCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.STUDY_CAFE, id);
    }

//    @GetMapping("/collect")
    public void collect(){
        placeDataCollector.collectAll();
    }

}

