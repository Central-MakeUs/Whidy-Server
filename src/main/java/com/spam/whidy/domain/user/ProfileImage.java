package com.spam.whidy.domain.user;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDateTime;

@Embeddable
public class ProfileImage {
    protected String profileImageKey;
    @Column(length = 2048)
    protected String profileImageUrl;
    protected LocalDateTime urlExpireDateTime;
}
