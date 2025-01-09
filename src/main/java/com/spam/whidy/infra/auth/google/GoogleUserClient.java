package com.spam.whidy.infra.auth.google;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClient;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.infra.auth.google.config.GoogleAuthConfig;
import com.spam.whidy.infra.auth.google.response.GoogleToken;
import com.spam.whidy.infra.auth.google.response.GoogleUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
@RequiredArgsConstructor
public class GoogleUserClient implements OauthUserClient {

    private final GoogleApiClient googleApiClient;
    private final GoogleAuthConfig authConfig;

    @Override
    public OAuthType support() {
        return OAuthType.GOOGLE;
    }

    @Override
    public User findUser(String code) {
        GoogleToken token = googleApiClient.fetchToken(makeRequestTokenParam(code));
        GoogleUserResponse userResponse = googleApiClient.fetchUser("Bearer " + token.getAccessToken());
        return userResponse.toEntity();
    }

    private MultiValueMap<String, String> makeRequestTokenParam(String code){
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("code", code);
        param.add("client_id", authConfig.getClientId());
        param.add("client_secret", authConfig.getClientSecret());
        param.add("grant_type", authConfig.getAuthorizationGrantType());
        param.add("redirect_uri", authConfig.getRedirectUri());
        return param;
    }
}
