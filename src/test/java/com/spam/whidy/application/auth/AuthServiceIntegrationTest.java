package com.spam.whidy.application.auth;

import com.spam.whidy.application.UserService;
import com.spam.whidy.application.auth.exception.SignInFailException;
import com.spam.whidy.domain.auth.SignUpInfo;
import com.spam.whidy.domain.auth.SignUpInfoRepository;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClientComposite;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProviderComposite;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.dto.auth.SignInResponse;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.testConfig.EmbeddedRedisConfig;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AuthServiceIntegrationTest extends IntegrationTest{

    @Autowired
    private AuthService authService;

    @Autowired
    private SignUpInfoRepository signUpInfoRepository;

    @Autowired
    private UserService userService;

    @MockBean
    private AuthCodeRequestUrlProviderComposite authCodeRequestUrlProviderComposite;

    @MockBean
    private OauthUserClientComposite oauthUserClientComposite;

    @Test
    @DisplayName("OAuth 로그인 - 기존 유저 로그인 성공")
    void signIn_existingUser_success() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String code = "test-oauth-code";
        String oauthId = "oauth-user-id";
        String email = "test@example.com";
        String name = "Test User";

        // 기존 유저 저장
        User existingUser = User.of(email, name, oauthType, oauthId);
        userService.save(existingUser);

        // OAuth 서버 응답 Mock
        Mockito.when(oauthUserClientComposite.findOauthUser(oauthType, code))
                .thenReturn(existingUser);

        // When
        SignInResponse response = authService.signIn(oauthType, code);

        // Then
        assertThat(response.authToken()).isNotNull();
        assertThat(response.userId()).isEqualTo(existingUser.getId());
    }

    @Test
    @DisplayName("OAuth 로그인 - 신규 유저 회원가입 코드 반환")
    void signIn_newUser_signUpCodeReturned() {
        // Given
        OAuthType oauthType = OAuthType.KAKAO;
        String code = "new-oauth-code";
        String oauthId = "new-oauth-id";
        User newUser = User.of("new@example.com", "New User", oauthType, oauthId);

        Mockito.when(oauthUserClientComposite.findOauthUser(oauthType, code))
                .thenReturn(newUser);

        // When & Then
        SignInFailException exception = assertThrows(SignInFailException.class, () -> {
            authService.signIn(oauthType, code);
        });

        // SignUpInfo에 저장됐는지 확인
        Optional<SignUpInfo> signUpInfo = signUpInfoRepository.findByCode(exception.getSignUpCode());
        assertThat(signUpInfo).isPresent();
        assertThat(signUpInfo.get().oauthId()).isEqualTo(oauthId);
    }

    @Test
    @DisplayName("회원가입 - 유효한 signUpCode로 회원가입 성공")
    void signUp_validSignUpCode_success() {
        // Given
        OAuthType oauthType = OAuthType.NAVER;
        String oauthId = "signUp-oauth-id";
        String signUpCode = UUID.randomUUID().toString();

        // 사전 회원가입 정보 저장
        signUpInfoRepository.save(signUpCode, new SignUpInfo(oauthType, oauthId));

        // When
        SignInResponse response = authService.signUp(signUpCode, "signup@example.com", "Sign Up User");

        // Then
        assertThat(response.authToken()).isNotNull();
        assertThat(response.userId()).isNotNull();

        // DB에 유저가 저장됐는지 확인
        Optional<User> savedUser = userService.findByEmail("signup@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getOauthId()).isEqualTo(oauthId);
    }

    @Test
    @DisplayName("회원가입 - 중복 이메일로 회원가입 실패")
    void signUp_duplicateEmail_fail() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String oauthId = "duplicate-oauth-id";
        String email = "duplicate@example.com";

        // 기존 유저 저장
        userService.save(User.of(email, "Existing User", oauthType, oauthId));

        String signUpCode = UUID.randomUUID().toString();
        signUpInfoRepository.save(signUpCode, new SignUpInfo(oauthType, oauthId));

        // When & Then
        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            authService.signUp(signUpCode, email, "New User");
        });

        assertThat(exception.getCode()).isEqualTo(ExceptionType.DUPLICATED_USER.getCode());
    }

    @Test
    @DisplayName("OAuth 인증 URL 생성 테스트")
    void getOauthCodeRequestPageUrl_test() {
        // Given
        OAuthType oauthType = OAuthType.GOOGLE;
        String mockState = UUID.randomUUID().toString();
        String mockUrl = "https://oauth.google.com/auth?state=" + mockState;

        Mockito.when(authCodeRequestUrlProviderComposite.getOauthCodeRequestUrl(Mockito.eq(oauthType), Mockito.anyString()))
                .thenReturn(mockUrl);

        // When
        String resultUrl = authService.getOauthCodeRequestPageUrl(oauthType);

        // Then
        assertThat(resultUrl).isEqualTo(mockUrl);
    }
}