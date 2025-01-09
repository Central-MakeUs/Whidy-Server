package com.spam.whidy.infra.auth.kakao;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClient;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.infra.auth.kakao.config.KakaoAuthConfig;
import com.spam.whidy.infra.auth.kakao.response.KakaoToken;
import com.spam.whidy.infra.auth.kakao.response.KakaoUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class KakaoUserClient implements OauthUserClient {

    private final KakaoApiClient kakaoApiClient;
    private final KakaoAuthConfig authConfig;

    @Override
    public OAuthType support() {
        return OAuthType.KAKAO;
    }

    @Override
    public User findUser(String code) {
        KakaoToken token = kakaoApiClient.fetchToken(makeRequestTokenParam(code));
        KakaoUserResponse userResponse = kakaoApiClient.fetchUser("Bearer " + token.getAccessToken(), createUserParam());
        return userResponse.toEntity();
    }

    private MultiValueMap<String, String> createUserParam() {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        return param;
    }

    private MultiValueMap<String, String> makeRequestTokenParam(String code){
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("code", code);
        param.add("client_id", authConfig.getClientId());
        param.add("client_secret", authConfig.getClientSecret());
        param.add("redirect_uri", authConfig.getRedirectUri());
        param.add("grant_type", authConfig.getAuthorizationGrantType());
        return param;
    }
}
