package com.spam.whidy.application.auth;

import com.spam.whidy.application.UserService;
import com.spam.whidy.application.auth.exception.SignInFailException;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.auth.SignUpInfo;
import com.spam.whidy.domain.auth.SignUpInfoRepository;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClientComposite;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProviderComposite;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.auth.SignInResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;
    @Mock
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;
    @Mock
    private OauthUserClientComposite oauthUserClientComposite;
    @Mock
    private AuthTokenService authTokenService;
    @Mock
    private UserService userService;
    @Mock
    private SignUpInfoRepository signUpInfoRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("OAuth 인증 URL 생성 성공")
    void getOauthCodeRequestPageUrl_success() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String expectedUrl = "https://oauth.google.com/auth";

        when(authCodeRequestUrlProviderComposite.getOauthCodeRequestUrl(eq(oauthType), anyString()))
                .thenReturn(expectedUrl);

        // When
        String result = authService.getOauthCodeRequestPageUrl(oauthType);

        // Then
        assertThat(result).isEqualTo(expectedUrl);
        verify(authCodeRequestUrlProviderComposite, times(1))
                .getOauthCodeRequestUrl(eq(oauthType), anyString());
    }

    @Test
    @DisplayName("기존 유저 OAuth 로그인 성공")
    void signIn_existingUser_success() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String code = "test-code";
        String oauthId = "oauth-user-id";

        User oauthUser = User.of("test@example.com", "Test User", oauthType, oauthId);
        AuthToken authToken = new AuthToken("access-token", "refresh-token");

        when(oauthUserClientComposite.findOauthUser(oauthType, code)).thenReturn(oauthUser);
        when(userService.findByAuthTypeAndAuthId(oauthType, oauthId)).thenReturn(Optional.of(oauthUser));
        when(authTokenService.createAuthToken(oauthUser.getId())).thenReturn(authToken);

        // When
        SignInResponse response = authService.signIn(oauthType, code);

        // Then
        assertThat(response.authToken()).isEqualTo(authToken);
        assertThat(response.userId()).isEqualTo(oauthUser.getId());
    }

    @Test
    @DisplayName("신규 유저 OAuth 로그인 - 회원가입 코드 반환")
    void signIn_newUser_returnsSignUpCode() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String code = "new-user-code";
        String oauthId = "new-oauth-id";

        User oauthUser = User.of("new@example.com", "New User", oauthType, oauthId);

        when(oauthUserClientComposite.findOauthUser(oauthType, code)).thenReturn(oauthUser);
        when(userService.findByAuthTypeAndAuthId(oauthType, oauthId)).thenReturn(Optional.empty());

        // When & Then
        SignInFailException exception = assertThrows(SignInFailException.class, () -> {
            authService.signIn(oauthType, code);
        });

        verify(signUpInfoRepository, times(1))
                .save(eq(exception.getSignUpCode()), any(SignUpInfo.class));
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() {
        // Given
        String signUpCode = UUID.randomUUID().toString();
        OAuthType oauthType = OAuthType.KAKAO;
        String oauthId = "sign-up-oauth-id";
        String email = "signup@example.com";
        String name = "Sign Up User";

        SignUpInfo signUpInfo = new SignUpInfo(oauthType, oauthId);
        User newUser = User.of(email, name, oauthType, oauthId);
        AuthToken authToken = new AuthToken("access-token", "refresh-token");

        when(signUpInfoRepository.findByCode(signUpCode)).thenReturn(Optional.of(signUpInfo));
        when(authTokenService.createAuthToken(newUser.getId())).thenReturn(authToken);

        // When
        SignInResponse response = authService.signUp(signUpCode, email, name);

        // Then
        assertThat(response.authToken()).isEqualTo(authToken);
        assertThat(response.userId()).isEqualTo(newUser.getId());
    }

    @Test
    @DisplayName("중복 회원가입 시 예외 발생")
    void signUp_duplicateUser_throwsException() {
        // Given
        String signUpCode = UUID.randomUUID().toString();
        OAuthType oauthType = OAuthType.NAVER;
        String oauthId = "duplicate-oauth-id";
        String email = "duplicate@example.com";

        SignUpInfo signUpInfo = new SignUpInfo(oauthType, oauthId);

        when(signUpInfoRepository.findByCode(signUpCode)).thenReturn(Optional.of(signUpInfo));
        when(userService.findByEmail(email)).thenReturn(Optional.of(mock(User.class)));

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.signUp(signUpCode, email, "Duplicated User");
        });

        assertThat(exception.getCode()).isEqualTo(ExceptionType.DUPLICATED_USER.getCode());
    }
}