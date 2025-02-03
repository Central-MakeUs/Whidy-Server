package com.spam.whidy.common.config.auth;

import com.spam.whidy.common.config.jwt.TokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.ServletWebRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthArgumentResolverTest {

    @InjectMocks private AuthArgumentResolver authArgumentResolver;

    @Mock private TokenUtil tokenUtil;
    @Mock private MethodParameter methodParameter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("supportsParameter - @Auth 어노테이션이 있는 경우 true 반환")
    void supportsParameter_shouldReturnTrue_whenParameterHasAuthAnnotation() {
        // Given
        when(methodParameter.hasParameterAnnotation(Auth.class)).thenReturn(true);

        // When
        boolean result = authArgumentResolver.supportsParameter(methodParameter);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("supportsParameter - @Auth 어노테이션이 없는 경우 false 반환")
    void supportsParameter_shouldReturnFalse_whenParameterHasNoAuthAnnotation() {
        // Given
        when(methodParameter.hasParameterAnnotation(Auth.class)).thenReturn(false);

        // When
        boolean result = authArgumentResolver.supportsParameter(methodParameter);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("resolveArgument - 요청에서 유저 ID를 올바르게 반환")
    void resolveArgument_shouldReturnLoginUser_whenValidToken() {
        // Given
        Long expectedUserId = 123L;
        MockHttpServletRequest request = new MockHttpServletRequest();
        NativeWebRequest webRequest = new ServletWebRequest(request);

        when(tokenUtil.getUserIdFromRequest(request)).thenReturn(expectedUserId);

        // When
        LoginUser loginUser = authArgumentResolver.resolveArgument(
                methodParameter, null, webRequest, null);

        // Then
        assertThat(loginUser).isNotNull();
        assertThat(loginUser.getUserId()).isEqualTo(expectedUserId);
    }

    @Test
    @DisplayName("resolveArgument - 토큰이 없을 경우 예외 발생")
    void resolveArgument_shouldThrowException_whenTokenIsInvalid() {
        // Given
        MockHttpServletRequest request = new MockHttpServletRequest();
        NativeWebRequest webRequest = new ServletWebRequest(request);

        when(tokenUtil.getUserIdFromRequest(request)).thenThrow(new RuntimeException("Invalid Token"));

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            authArgumentResolver.resolveArgument(methodParameter, null, webRequest, null);
        });
    }
}