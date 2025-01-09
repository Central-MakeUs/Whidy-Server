package com.spam.whidy.infra.auth.kakao;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProvider;
import com.spam.whidy.infra.auth.kakao.config.KakaoAuthConfig;
import com.spam.whidy.infra.auth.kakao.config.KakaoProviderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class KakaoAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final KakaoProviderConfig providerConfig;
    private final KakaoAuthConfig authConfig;

    @Override
    public OAuthType support() {
        return OAuthType.KAKAO;
    }

    @Override
    public String getUrl(String state) {
        return UriComponentsBuilder
                .fromUriString(providerConfig.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", authConfig.getClientId())
                .queryParam("redirect_uri", authConfig.getRedirectUri())
//                .queryParam("scope", authConfig.getScope())
                .queryParam("state",state)
                .toUriString();
    }
}
