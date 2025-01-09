package com.spam.whidy.infra.auth.google.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GoogleUserResponse {

    private String sub; // Google 사용자 ID
    private String name; // 사용자 이름
    private String givenName; // 이름
    private String familyName; // 성
    private String email;
    private String picture; // 프로필 사진 URL

    public User toEntity(){
        return User.builder()
                .oauthType(OAuthType.GOOGLE)
                .oauthId(sub)
                .name(name)
                .build();
    }

}