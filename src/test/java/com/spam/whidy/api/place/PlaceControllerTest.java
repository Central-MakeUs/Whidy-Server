package com.spam.whidy.api.place;

import com.spam.whidy.application.place.PlaceService;
import com.spam.whidy.domain.place.PlaceType;
import com.spam.whidy.dto.place.CafeDTO;
import com.spam.whidy.dto.place.PlaceDTO;
import com.spam.whidy.dto.place.PlaceSearchCondition;
import com.spam.whidy.testConfig.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PlaceController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaceControllerTest extends ControllerTest {

    @MockBean private PlaceService placeService;

    @Test
    @DisplayName("일반 카페 조회 성공")
    void findCafeById_Success() throws Exception {
        CafeDTO cafeDTO = CafeDTO.builder()
                .id(1L)
                .name("테스트 카페")
                .address("서울시 강남구")
                .latitude(37.12345)
                .longitude(127.12345)
                .build();

        given(placeService.findCafe(PlaceType.GENERAL_CAFE, 1L)).willReturn(cafeDTO);

        mockMvc.perform(get("/api/place/general-cafe/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("테스트 카페"))
                .andExpect(jsonPath("$.address").value("서울시 강남구"));
    }

    @Test
    @DisplayName("프랜차이즈 카페 조회 성공")
    void findFranchiseCafeById_Success() throws Exception {
        CafeDTO cafeDTO = CafeDTO.builder()
                .id(2L)
                .name("프랜차이즈 카페")
                .address("서울시 서초구")
                .latitude(37.54321)
                .longitude(127.54321)
                .build();

        given(placeService.findCafe(PlaceType.FRANCHISE_CAFE, 2L)).willReturn(cafeDTO);

        mockMvc.perform(get("/api/place/franchise-cafe/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("프랜차이즈 카페"));
    }

    @Test
    @DisplayName("조건에 따른 장소 검색 성공")
    void searchByCondition_Success() throws Exception {
        PlaceDTO placeDTO = PlaceDTO.builder()
                .id(1L)
                .name("스터디 카페")
                .address("서울시 강남구")
                .latitude(37.12345)
                .longitude(127.12345)
                .build();

        given(placeService.searchByCondition(any(PlaceSearchCondition.class)))
                .willReturn(List.of(placeDTO));

        mockMvc.perform(get("/api/place")
                        .param("centerLatitude", "37.0")
                        .param("centerLongitude", "127.0")
                        .param("radius", "500"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("스터디 카페"));
    }
}