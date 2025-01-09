package com.spam.whidy.domain.auth;

public record AuthToken(
        String accessToken,
        String refreshToken
){
    public String getAccessToken(){
        return accessToken;
    }

    public String getRefreshToken(){
        return refreshToken;
    }
}
