package com.spam.whidy.api.admin;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.application.place.PlaceDataCollectService;
import com.spam.whidy.application.place.PlaceRequestService;
import com.spam.whidy.application.user.UserFinder;
import com.spam.whidy.application.user.UserService;
import com.spam.whidy.domain.user.Role;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.place.PlaceRequestSearchCondition;
import com.spam.whidy.dto.user.GrantRoleRequest;
import com.spam.whidy.testConfig.ControllerTest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest extends ControllerTest {

    @Autowired private ObjectMapper objectMapper;

    @MockBean private UserFinder userFinder;
    @MockBean private UserService userService;
    @MockBean private AuthTokenService authTokenService;
    @MockBean private PlaceRequestService placeRequestService;
    @MockBean private PlaceDataCollectService placeDataCollectService;

    private final Long SUPER_ADMIN_USER_ID = 1L;
    private final Long ADMIN_USER_ID = 2L;
    private final Long GENERAL_USER_ID = 3L;

    private final User superAdminUser = User.builder()
            .id(SUPER_ADMIN_USER_ID)
            .role(Role.SUPER_ADMIN)
            .build();

    private final User adminUser = User.builder()
            .id(ADMIN_USER_ID)
            .role(Role.ADMIN)
            .build();

    private final User generalUser = User.builder()
            .id(GENERAL_USER_ID)
            .role(Role.USER)
            .build();

    @BeforeEach
    void setUp() {
        when(userFinder.findById(SUPER_ADMIN_USER_ID)).thenReturn(Optional.of(superAdminUser));
        when(userFinder.findById(ADMIN_USER_ID)).thenReturn(Optional.of(adminUser));
        when(userFinder.findById(GENERAL_USER_ID)).thenReturn(Optional.of(generalUser));
    }

    @Test
    @DisplayName("사용자 검색 성공 (슈파관리자)")
    void searchUsers_Success() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(SUPER_ADMIN_USER_ID);

        mockMvc.perform(get("/api/admin/user/search")
                        .param("name", "홍길동"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("사용자 검색 권한없어 실패 (관리자)")
    void searchUser_admin() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(ADMIN_USER_ID);

        mockMvc.perform(get("/api/admin/user/search")
                        .param("name", "홍길동"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 검색 권한없어 실패 (일반사용자)")
    void searchUser_generalUser() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(GENERAL_USER_ID);

        mockMvc.perform(get("/api/admin/user/search")
                        .param("name", "홍길동"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("사용자 권한 부여 성공")
    void grantPrivilegeToUser_Success() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(SUPER_ADMIN_USER_ID);
        doNothing().when(userService).grantRole(any(Long.class), any(Role.class));

        GrantRoleRequest request = new GrantRoleRequest(ADMIN_USER_ID, Role.ADMIN);

        mockMvc.perform(post("/api/admin/user/grant")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장소 요청 목록 조회 성공")
    void searchPlaceRequest_Success() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(ADMIN_USER_ID);
        when(placeRequestService.searchByCondition(any(PlaceRequestSearchCondition.class))).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/admin/place-request/search")
                        .param("address", "서울"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("장소 크롤링 실행 성공")
    void collectPlaceData_Success() throws Exception {
        given(tokenUtil.getUserIdFromRequest(any(HttpServletRequest.class))).willReturn(ADMIN_USER_ID);
        doNothing().when(placeDataCollectService).collectAll();

        mockMvc.perform(post("/api/admin/place/collect"))
                .andExpect(status().isOk());
    }
}