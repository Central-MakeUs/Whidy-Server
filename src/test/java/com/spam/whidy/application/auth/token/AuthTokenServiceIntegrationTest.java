package com.spam.whidy.application.auth.token;

import com.spam.whidy.common.config.jwt.TokenUtil;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.auth.AuthTokenRepository;
import com.spam.whidy.dto.auth.SignOutRequest;
import com.spam.whidy.testConfig.IntegrationTest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthTokenServiceIntegrationTest extends IntegrationTest {

    @Autowired private AuthTokenService authTokenService;

    @MockBean private TokenUtil tokenUtil;
    @MockBean private AuthTokenRepository authTokenRepository;

    private final Long userId = 1L;
    private final String accessToken = "mock-access-accessToken";
    private final String refreshToken = "mock-refresh-accessToken";

    @BeforeEach
    void setUp() {
        when(tokenUtil.generateAccessToken(userId)).thenReturn(accessToken);
        when(tokenUtil.generateRefreshToken(userId)).thenReturn(refreshToken);
        when(tokenUtil.getExpireDateFromToken(anyString())).thenReturn(new Date(System.currentTimeMillis() + 100000));
        when(tokenUtil.getUserIdFromToken(refreshToken)).thenReturn(userId);
    }

    @Test
    @DisplayName("AccessToken 및 RefreshToken 생성 성공")
    void createAuthToken_success() {
        // When
        AuthToken token = authTokenService.createAuthToken(userId);

        // Then
        assertThat(token.getAccessToken()).isEqualTo(accessToken);
        assertThat(token.getRefreshToken()).isEqualTo(refreshToken);

        verify(authTokenRepository, times(1)).saveRefreshToken(eq(userId), eq(refreshToken), any(Date.class));
    }

    @Test
    @DisplayName("리프레시 토큰으로 AccessToken 재발급 성공")
    void refreshToken_success() {
        // Given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(tokenUtil.getUserIdFromRequest(mockRequest)).thenReturn(userId);
        when(tokenUtil.resolveTokenFromRequest(mockRequest)).thenReturn(refreshToken);
        when(authTokenRepository.getRefreshToken(userId)).thenReturn(refreshToken);

        // When
        AuthToken newToken = authTokenService.refreshToken(mockRequest);

        // Then
        assertThat(newToken.getAccessToken()).isEqualTo(accessToken);
        assertThat(newToken.getRefreshToken()).isEqualTo(refreshToken);

        verify(authTokenRepository, times(1)).getRefreshToken(userId);
    }

    @Test
    @DisplayName("잘못된 리프레시 토큰으로 AccessToken 재발급 실패")
    void refreshToken_invalidToken_fail() {
        // Given
        HttpServletRequest mockRequest = mock(HttpServletRequest.class);
        when(tokenUtil.getUserIdFromRequest(mockRequest)).thenReturn(userId);
        when(tokenUtil.resolveTokenFromRequest(mockRequest)).thenReturn("invalid-refresh-accessToken");
        when(authTokenRepository.getRefreshToken(userId)).thenReturn(refreshToken);

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authTokenService.refreshToken(mockRequest);
        });

        assertThat(exception.getCode()).isEqualTo(ExceptionType.TOKEN_NOT_VALID.getCode());
    }

    @Test
    @DisplayName("토큰 무효화 및 블랙리스트 등록 성공")
    void makeTokenInvalid_success() {
        // Given
        SignOutRequest request = new SignOutRequest(accessToken, refreshToken);

        // When
        authTokenService.makeTokenInvalid(request);

        // Then
        verify(authTokenRepository, times(1)).deleteRefreshToken(userId);
        verify(authTokenRepository, times(1)).addBlackList(eq(accessToken), any(Date.class));
    }

    @Test
    @DisplayName("유효한 토큰 검증 성공")
    void isTokenValid_validToken_success() {
        // Given
        when(tokenUtil.isTokenValid(accessToken)).thenReturn(true);
        when(authTokenRepository.isBlackListToken(accessToken)).thenReturn(false);

        // When
        boolean result = authTokenService.isTokenValid(accessToken);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("블랙리스트에 있는 토큰 검증 실패")
    void isTokenValid_blacklistedToken_fail() {
        // Given
        when(tokenUtil.isTokenValid(accessToken)).thenReturn(true);
        when(authTokenRepository.isBlackListToken(accessToken)).thenReturn(true);

        // When
        boolean result = authTokenService.isTokenValid(accessToken);

        // Then
        assertThat(result).isFalse();
    }
}