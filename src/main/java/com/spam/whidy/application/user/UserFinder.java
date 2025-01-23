package com.spam.whidy.application.user;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import com.spam.whidy.dto.user.UserSearchCondition;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserFinder {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public Optional<User> findByAuthTypeAndAuthId(OAuthType authType, String authId){
        return userRepository.findByOauthTypeAndOauthId(authType, authId);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> findByCondition(UserSearchCondition condition) {
        return userRepository.searchByCondition(condition);
    }

}
