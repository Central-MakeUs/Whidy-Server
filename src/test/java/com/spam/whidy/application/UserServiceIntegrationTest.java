package com.spam.whidy.application;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest  // Spring Boot의 전체 컨텍스트 로딩
@ActiveProfiles("test")  // 테스트 환경 프로파일 적용
@Transactional  // 각 테스트 후 롤백
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("유저 저장 테스트")
    void saveUserTest() {
        // Given
        User user = User.of("test@example.com", "Test User", OAuthType.GOOGLE, "oauth-id-123");

        // When
        userService.save(user);

        // Then
        Optional<User> savedUser = userRepository.findByEmail("test@example.com");
        assertThat(savedUser).isPresent();
        assertThat(savedUser.get().getName()).isEqualTo("Test User");
        assertThat(savedUser.get().getOauthType()).isEqualTo(OAuthType.GOOGLE);
    }

    @Test
    @DisplayName("OAuth 타입과 ID로 유저 조회 테스트")
    void findByAuthTypeAndAuthIdTest() {
        // Given
        User user = User.of("auth@example.com", "OAuth User", OAuthType.KAKAO, "oauth-id-456");
        userService.save(user);

        // When
        Optional<User> foundUser = userService.findByAuthTypeAndAuthId(OAuthType.KAKAO, "oauth-id-456");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("auth@example.com");
    }

    @Test
    @DisplayName("이메일로 유저 조회 테스트")
    void findByEmailTest() {
        // Given
        User user = User.of("email@example.com", "Email User", OAuthType.NAVER, "oauth-id-789");
        userService.save(user);

        // When
        Optional<User> foundUser = userService.findByEmail("email@example.com");

        // Then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getName()).isEqualTo("Email User");
        assertThat(foundUser.get().getOauthType()).isEqualTo(OAuthType.NAVER);
    }
}