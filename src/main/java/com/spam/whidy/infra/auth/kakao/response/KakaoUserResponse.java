package com.spam.whidy.infra.auth.kakao.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoUserResponse {

    private Long id;
    private KakaoAccount kakaoAccount;

    public User toEntity(){
        return User.builder()
                .oauthType(OAuthType.KAKAO)
                .oauthId(id.toString())
                .build();
    }

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class KakaoAccount{
        Profile profile;

        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class Profile{
            String nickname;
            String profileImageUrl;
        }
    }
}