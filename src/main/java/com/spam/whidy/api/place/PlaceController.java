package com.spam.whidy.api.place;

import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.dto.place.CafeDTO;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Tag(name = "장소 조회")
@RequiredArgsConstructor
@RequestMapping("/api/place")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/general-cafe/{id}")
    @Operation(summary = "일반 카페 조회")
    public CafeDTO findCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.GENERAL_CAFE, id);
    }

    @GetMapping("/franchise-cafe/{id}")
    @Operation(summary = "프랜차이즈 카페 조회")
    public CafeDTO findFranchiseCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.FRANCHISE_CAFE, id);
    }

    @GetMapping("/study-cafe/{id}")
    @Operation(summary = "스터디 카페 조회")
    public CafeDTO findStudyCafeById(@PathVariable Long id){
        return placeService.findCafe(PlaceType.STUDY_CAFE, id);
    }

    @GetMapping
    @Operation(summary = "장소 조회", description = """
            <b>* 위경도 값 필수</b> <br> <b>latitudeFrom</b> : 남서쪽 위도<br> <b>longitudeFrom</b> : 남서쪽 경도<br> <b>latitudeTo</b> : 북동쪽 위도<br> <b>longitudeTo</b> : 북동쪽 경도<br><br>
            <b>businessTime</b> : </b> ex) 13:00 <br>
            <b>placeType (List)</b> : STUDY_CAFE, GENERAL_CAFE, FRANCHISE_CAFE, FREE_PICTURE, FREE_STUDY_SPACE, INTERVIEW_CLOTHES_RENTAL <br>
            <b>businessDayOfWeek (List)</b> : MONDAY, TUESDAY, WEDNESDAY, ...
            """)
    public List<PlaceDTO> searchByCondition(PlaceSearchCondition condition){
        return placeService.searchByCondition(condition);
    }

}

