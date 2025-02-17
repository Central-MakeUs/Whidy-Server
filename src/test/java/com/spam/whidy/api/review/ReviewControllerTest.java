package com.spam.whidy.api.review;

import com.spam.whidy.application.review.ReviewService;
import com.spam.whidy.domain.review.Review;
import com.spam.whidy.dto.review.ReviewRequest;
import com.spam.whidy.testConfig.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.spam.whidy.dto.review.ReviewSearchCondition;

@WebMvcTest(ReviewController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReviewControllerTest extends ControllerTest{

    @MockBean private ReviewService reviewService;

    @Test
    @DisplayName("장소 리뷰 조회 성공")
    void findByPlaceId_Success() throws Exception {
        ReviewSearchCondition condition = new ReviewSearchCondition(1L, 0, 10);
        when(reviewService.searchByCondition(any())).thenReturn(List.of());

        mockMvc.perform(get("/api/review")
                        .param("placeId", "1")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장소 리뷰 조회 실패 (필수 파라미터 누락)")
    void findByPlaceId_Failure_MissingParam() throws Exception {
        mockMvc.perform(get("/api/review")
                        .param("offset", "0")
                        .param("limit", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("리뷰 ID로 조회 성공")
    void findById_Success() throws Exception {
        when(reviewService.findById(any())).thenReturn(Mockito.mock(Review.class));

        mockMvc.perform(get("/api/review/{reviewId}", 1L))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 저장 성공")
    void save_Success() throws Exception {
        ReviewRequest request = new ReviewRequest(1L, 1L, 5.0f, Set.of(), false, null, Set.of(), null);
        when(reviewService.save(any(), any())).thenReturn(1L);

        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 저장 실패 (유효하지 않은 데이터)")
    void save_Failure_InvalidData() throws Exception {
        ReviewRequest request = new ReviewRequest(null, null, null, null, false, null, null, null);

        mockMvc.perform(post("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("리뷰 업데이트 성공")
    void update_Success() throws Exception {
        ReviewRequest request = new ReviewRequest(1L, 1L, 4.0f, Set.of(), true, null, Set.of(), null);
        doNothing().when(reviewService).update(any(), any());

        mockMvc.perform(put("/api/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("리뷰 삭제 성공")
    void delete_Success() throws Exception {
        doNothing().when(reviewService).delete(any(), any());

        mockMvc.perform(delete("/api/review/{reviewId}", 1L))
                .andExpect(status().isOk());
    }

}