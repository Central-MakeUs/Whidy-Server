package com.spam.whidy.api.place;


import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.dto.place.PlaceRequestDTO;
import com.spam.whidy.testConfig.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(PlaceRequestController.class)
@AutoConfigureMockMvc(addFilters = false)
class PlaceRequestControllerTest extends ControllerTest {

    @MockBean private PlaceRequestService placeRequestService;

    @Test
    @DisplayName("PlaceRequest 저장 성공")
    void savePlaceRequest_Success() throws Exception {
        PlaceRequestDTO requestDTO = new PlaceRequestDTO(
                "서울특별시 강남구",
                "스타벅스 강남점",
                37.4979,
                127.0276
        );

        mockMvc.perform(post("/api/place-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andDo(print());

        verify(placeRequestService, times(1)).save(requestDTO);
    }

    @Test
    @DisplayName("PlaceRequest 저장 실패 - 유효하지 않은 데이터")
    void savePlaceRequest_Failure_InvalidData() throws Exception {
        PlaceRequestDTO invalidDTO = new PlaceRequestDTO(
                null,
                "스타벅스 강남점",
                37.4979,
                127.0276
        );

        mockMvc.perform(post("/api/place-request")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.argumentMessages.address").exists())
                .andDo(print());

        verify(placeRequestService, times(0)).save(any());
    }
}