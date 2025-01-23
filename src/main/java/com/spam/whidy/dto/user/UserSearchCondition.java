package com.spam.whidy.dto.user;

import com.spam.whidy.domain.user.Role;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record UserSearchCondition (
        String name,
        String email,
        Role role,
        LocalDateTime joinDateFrom,
        LocalDateTime joinDateTo
) { }
