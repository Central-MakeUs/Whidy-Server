package com.spam.whidy.application.user;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.domain.user.Role;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.domain.user.UserRepository;
import com.spam.whidy.dto.user.ProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ProfileImageStorage profileImageStorage;

    @Value("${application.profile.url-ttl-minute}")
    private long profileImageUrlTtlMinute;

    public void save(User user){
        userRepository.save(user);
    }

    public void updateProfileImage(Long userId, MultipartFile file) {
        User user = findUserById(userId);
        String imageKey = null;
        if(!file.isEmpty()){
            imageKey = profileImageStorage.upload(file, userId);
        }
        user.updateProfileKey(imageKey);
    }

    public ProfileResponse getProfile(Long userId){
        User user = findUserById(userId);
        if(user.hasProfileImage() && !user.isProfileImageUrlValid()){
            String newImageUrl = profileImageStorage.getImageUrl(user.getProfileImageKey(), profileImageUrlTtlMinute);
            LocalDateTime urlExpirationDateTime = LocalDateTime.now().plusMinutes(profileImageUrlTtlMinute);
            user.updateProfileImageUrl(newImageUrl, urlExpirationDateTime);
        }
        return new ProfileResponse(user.getName(), user.getProfileImageUrl());
    }

    public void updateRole(Long userId, Role role) {
        User user = findUserById(userId);
        user.updateRole(role);
    }

    public void updateProfile(Long userId, String name) {
        User user = findUserById(userId);
        user.updateName(name);
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ExceptionType.USER_NOT_FOUND));
    }
}
