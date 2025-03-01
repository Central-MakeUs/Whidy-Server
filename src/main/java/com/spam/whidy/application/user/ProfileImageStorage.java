package com.spam.whidy.application.user;

import org.springframework.web.multipart.MultipartFile;

public interface ProfileImageStorage {
    String upload(MultipartFile file, Long userId);
    String getImageUrl(String key, Long ttlMinute);
}
