package com.spam.whidy.testConfig;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
public abstract class IntegrationTest {

    @Autowired UserRepository userRepository;

    public User testUser;

    @BeforeEach
    public void setTestUser(){
        User user = User.builder()
                .name("testUser1")
                .email("testUser1@test.com")
                .oauthId("oauth-id")
                .oauthType(OAuthType.KAKAO)
                .build();
        userRepository.save(user);
        testUser = user;
    }
}
