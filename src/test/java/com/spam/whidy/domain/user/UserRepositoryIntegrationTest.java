package com.spam.whidy.domain.user;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.dto.user.UserSearchCondition;
import com.spam.whidy.testConfig.IntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryIntegrationTest extends IntegrationTest {

    @Autowired private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        List<User> intiUsers = userRepository.findAll();
        userRepository.deleteAll(intiUsers);

        User user1 = User.builder()
                .name("홍길동")
                .email("hong@test.com")
                .joinDateTime(LocalDateTime.of(2023, 1, 15, 10, 0))
                .role(Role.USER)
                .oauthId("a")
                .oauthType(OAuthType.KAKAO)
                .build();

        User user2 = User.builder()
                .name("이순신")
                .email("lee@test.com")
                .joinDateTime(LocalDateTime.of(2023, 3, 10, 15, 0))
                .role(Role.ADMIN)
                .oauthId("a")
                .oauthType(OAuthType.KAKAO)
                .build();

        User user3 = User.builder()
                .name("홍길동")
                .email("hong2@test.com")
                .joinDateTime(LocalDateTime.of(2024, 1, 5, 12, 0))
                .role(Role.USER)
                .oauthId("a")
                .oauthType(OAuthType.KAKAO)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
    }

    @Test
    @DisplayName("이름과 역할 조건 검색 테스트")
    void searchByNameAndRole() {
        UserSearchCondition condition = UserSearchCondition.builder()
                .name("홍길동")
                .role(Role.USER)
                .build();

        List<User> results = userRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name").containsOnly("홍길동");
        assertThat(results).extracting("role").containsOnly(Role.USER);
    }

    @Test
    @DisplayName("이메일 부분 검색 테스트")
    void searchByEmail() {
        UserSearchCondition condition = UserSearchCondition.builder()
                .email("hong")
                .build();

        List<User> results = userRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);
        assertThat(results).extracting("email").contains("hong@test.com", "hong2@test.com");
    }

    @Test
    @DisplayName("가입일 범위 검색 테스트")
    void searchByJoinDateRange() {
        UserSearchCondition condition = UserSearchCondition.builder()
                .joinDateFrom(LocalDateTime.of(2023, 1, 1, 0, 0))
                .joinDateTo(LocalDateTime.of(2023, 12, 31, 23, 59))
                .build();

        List<User> results = userRepository.searchByCondition(condition);

        assertThat(results).isNotEmpty();
        assertThat(results).hasSize(2);
        assertThat(results).extracting("name").contains("홍길동", "이순신");
    }

    @Test
    @DisplayName("조건 없이 전체 조회 테스트")
    void searchWithoutCondition() {
        UserSearchCondition condition = UserSearchCondition.builder().build();

        List<User> results = userRepository.searchByCondition(condition);

        assertThat(results).hasSize(3);
    }
}