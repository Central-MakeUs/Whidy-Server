package com.spam.whidy.domain.user;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByOauthTypeAndOauthId(OAuthType oauthType, String oauthId);
    Optional<User> findByEmail(String email);
}
