package com.spam.whidy.api.place;

import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.dto.place.PlaceRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "장소 등록 요청")
@RequiredArgsConstructor
@RequestMapping("/api/place-request")
public class PlaceRequestController {

    private final PlaceRequestService placeRequestService;

    @PostMapping
    @Operation(summary = "장소 등록 요청")
    public void save(@Valid @RequestBody PlaceRequestDTO dto){
        placeRequestService.save(dto);
    }
}
