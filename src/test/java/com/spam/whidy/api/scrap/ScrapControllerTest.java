package com.spam.whidy.api.scrap;

import com.spam.whidy.application.scrap.ScrapService;
import com.spam.whidy.common.config.auth.LoginUser;
import com.spam.whidy.dto.scrap.ScrapDTO;
import com.spam.whidy.dto.scrap.ScrapRequest;
import com.spam.whidy.dto.scrap.ScrapSearchCondition;
import com.spam.whidy.testConfig.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ScrapController.class)
@AutoConfigureMockMvc(addFilters = false)
class ScrapControllerTest extends ControllerTest {

    @MockBean private ScrapService scrapService;

    @Test
    @DisplayName("스크랩 저장 성공")
    void saveScrapSuccess() throws Exception {
        ScrapRequest request = new ScrapRequest(1L);
        Long userId = 1L;

        Mockito.when(scrapService.save(eq(userId), any(ScrapRequest.class))).thenReturn(1L);

        mockMvc.perform(post("/api/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스크랩 삭제 성공")
    void deleteScrapSuccess() throws Exception {
        Long scrapId = 1L;
        Long userId = 1L;

        mockMvc.perform(delete("/api/scrap/{scrapId}", scrapId)
                        .requestAttr("loginUser", new LoginUser(userId)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스크랩 조건 조회 성공")
    void searchByConditionSuccess() throws Exception {
        ScrapSearchCondition condition = new ScrapSearchCondition("test", 0, 10);
        Long userId = 1L;

        Mockito.when(scrapService.searchByConditionAndUser(eq(userId), any(ScrapSearchCondition.class)))
                .thenReturn(List.of(new ScrapDTO(1L, null)));

        mockMvc.perform(get("/api/scrap")
                        .queryParam("keyword", condition.keyword())
                        .queryParam("offset", String.valueOf(condition.offset()))
                        .queryParam("limit", String.valueOf(condition.limit())))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("스크랩 저장 실패 - 잘못된 요청 데이터")
    void saveScrapFail() throws Exception {
        ScrapRequest request = new ScrapRequest(null);

        mockMvc.perform(post("/api/scrap")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

}