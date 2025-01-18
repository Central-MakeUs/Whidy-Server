package com.spam.whidy.dto.user;

import com.spam.whidy.domain.user.Role;
import jakarta.validation.constraints.NotNull;

public record GrantRoleRequest(
    @NotNull Long userId,
    @NotNull Role role
){ }
