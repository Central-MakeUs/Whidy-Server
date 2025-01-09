package com.spam.whidy.application;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void save(User user){
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByAuthTypeAndAuthId(OAuthType authType, String authId){
        return userRepository.findByOauthTypeAndOauthId(authType, authId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
