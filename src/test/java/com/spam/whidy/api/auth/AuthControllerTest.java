package com.spam.whidy.api.auth;

import com.spam.whidy.domain.user.UserRepository;
import com.spam.whidy.dto.auth.SignOutRequest;
import com.spam.whidy.testConfig.ControllerTest;
import com.spam.whidy.application.auth.AuthService;
import com.spam.whidy.application.auth.exception.SignInFailException;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.dto.auth.SignInResponse;
import com.spam.whidy.dto.auth.SignUpRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest extends ControllerTest {

    @MockBean private AuthService authService;
    @MockBean private AuthTokenService authTokenService;
    @MockBean private UserRepository userRepository;

    @Value("${oauth.url.success}")
    private String successUrl;
    @Value("${oauth.url.fail}")
    private String failUrl;

    @Test
    @DisplayName("OAuth 로그인 성공 테스트")
    void testLoginSuccess() throws Exception {
        OAuthType oauthType = OAuthType.GOOGLE;
        String code = "test-code";
        Long userId = 1L;

        AuthToken mockToken = new AuthToken("access-accessToken", "refresh-accessToken");
        SignInResponse signInResponse = new SignInResponse(mockToken, userId);

        Mockito.when(authService.signIn(oauthType, code)).thenReturn(signInResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/callback/{oauthType}", oauthType)
                        .param("code", code))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(successUrl + "?accessToken=access-accessToken&refreshToken=refresh-accessToken"));

        Mockito.verify(authService).signIn(oauthType, code);
    }

    @Test
    @DisplayName("OAuth 로그인 실패 테스트")
    void testLoginFail() throws Exception {
        OAuthType oauthType = OAuthType.GOOGLE;
        String code = "test-code";

        SignInFailException signInFailException = new SignInFailException("signUp-code");
        Mockito.when(authService.signIn(oauthType, code)).thenThrow(signInFailException);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/callback/{oauthType}", oauthType)
                        .param("code", code))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(failUrl + "?signUpCode=signUp-code"));

        Mockito.verify(authService).signIn(oauthType, code);
    }

    @Test
    @DisplayName("OAuth 회원가입 테스트")
    void testSignUp() throws Exception {
        SignUpRequest request = new SignUpRequest("signUpCode", "test@example.com", "test-user");
        AuthToken mockToken = new AuthToken("access-accessToken", "refresh-accessToken");
        Long userId = 1L;
        SignInResponse signInResponse = new SignInResponse(mockToken, userId);

        Mockito.when(authService.signUp(request.signUpCode(), request.email(), request.name())).thenReturn(signInResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"signUpCode\": \"signUpCode\",\"email\":\"test@example.com\",\"name\":\"test-user\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authToken.accessToken").value("access-accessToken"))
                .andExpect(jsonPath("$.authToken.refreshToken").value("refresh-accessToken"));

        Mockito.verify(authService).signUp(request.signUpCode(), request.email(), request.name());
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void testSignOut() throws Exception {
        SignOutRequest request = new SignOutRequest("access-accessToken", "refresh-accessToken");

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-out")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accessToken\":\"access-accessToken\",\"refreshToken\":\"refresh-accessToken\"}"))
                .andExpect(status().isOk());

        Mockito.verify(authTokenService).makeTokenInvalid(request);
    }

    @Test
    @DisplayName("토큰 갱신 테스트")
    void testRefreshToken() throws Exception {
        AuthToken mockToken = new AuthToken("new-access-accessToken", "new-refresh-accessToken");

        Mockito.when(authTokenService.refreshToken(Mockito.any(HttpServletRequest.class))).thenReturn(mockToken);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/refresh-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-access-accessToken"))
                .andExpect(jsonPath("$.refreshToken").value("new-refresh-accessToken"));

        Mockito.verify(authTokenService).refreshToken(Mockito.any(HttpServletRequest.class));
    }

    @Test
    @DisplayName("OAuth 페이지로 리다이렉션 테스트")
    void testRedirectToOauthCodeRequestPage() throws Exception {
        OAuthType oauthType = OAuthType.GOOGLE;
        String pageUrl = "https://oauth.provider.com/auth";

        Mockito.when(authService.getOauthCodeRequestPageUrl(oauthType)).thenReturn(pageUrl);

        mockMvc.perform(MockMvcRequestBuilders.get("/auth/{oauthType}", oauthType))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(pageUrl));

        Mockito.verify(authService).getOauthCodeRequestPageUrl(oauthType);
    }
}