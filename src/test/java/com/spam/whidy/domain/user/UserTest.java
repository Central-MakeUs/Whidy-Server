package com.spam.whidy.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserTest {

    @Test
    @DisplayName("SUPER_ADMIN 권한 사용자는 isAdmin()이 true를 반환한다.")
    void isAdmin_WithSuperAdminRole_ShouldReturnTrue() {
        User superAdminUser = User.builder()
                .id(1L)
                .role(Role.SUPER_ADMIN)
                .build();

        assertThat(superAdminUser.isAdmin()).isTrue();
    }


    @Test
    @DisplayName("ADMIN 권한 사용자는 isAdmin()이 true를 반환한다.")
    void isAdmin_WithAdminRole_ShouldReturnTrue() {
        User adminUser = User.builder()
                .id(2L)
                .role(Role.ADMIN)
                .build();

        assertThat(adminUser.isAdmin()).isTrue();
    }

    @Test
    @DisplayName("일반 사용자는 isAdmin()이 false를 반환한다.")
    void isAdmin_WithUserRole_ShouldReturnFalse() {
        User normalUser = User.builder()
                .id(3L)
                .role(Role.USER)
                .build();

        assertThat(normalUser.isAdmin()).isFalse();
    }

    @Test
    @DisplayName("SUPER_ADMIN 권한 사용자는 isSuperAdmin()이 true를 반환한다.")
    void isSuperAdmin_WithSuperAdminRole_ShouldReturnTrue() {
        User superAdminUser = User.builder()
                .id(4L)
                .role(Role.SUPER_ADMIN)
                .build();

        assertThat(superAdminUser.isSuperAdmin()).isTrue();
    }

    @Test
    @DisplayName("ADMIN 권한 사용자는 isSuperAdmin()이 false를 반환한다.")
    void isSuperAdmin_WithAdminRole_ShouldReturnFalse() {
        User adminUser = User.builder()
                .id(5L)
                .role(Role.ADMIN)
                .build();

        assertThat(adminUser.isSuperAdmin()).isFalse();
    }

    @Test
    @DisplayName("일반 사용자는 isSuperAdmin()이 false를 반환한다.")
    void isSuperAdmin_WithUserRole_ShouldReturnFalse() {
        User normalUser = User.builder()
                .id(6L)
                .role(Role.USER)
                .build();

        assertThat(normalUser.isSuperAdmin()).isFalse();
    }
}