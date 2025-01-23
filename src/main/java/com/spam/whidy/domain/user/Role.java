package com.spam.whidy.domain.user;

public enum Role {

    USER, ADMIN, SUPER_ADMIN;

    public boolean isAdmin(){
        return this == Role.ADMIN || this == Role.SUPER_ADMIN;
    }

    public boolean isSuperAdmin(){
        return this == Role.SUPER_ADMIN;
    }
}
