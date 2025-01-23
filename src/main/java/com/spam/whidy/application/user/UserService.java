package com.spam.whidy.application.user;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.Role;
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
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public void save(User user){
        userRepository.save(user);
    }

    @Transactional
    public void grantRole(Long userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.USER_NOT_FOUND));
        user.updateRole(role);
    }
}
