package com.spam.whidy.infra.auth.apple;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProvider;
import com.spam.whidy.infra.auth.apple.config.AppleAuthConfig;
import com.spam.whidy.infra.auth.apple.config.AppleProviderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class AppleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final AppleProviderConfig providerConfig;
    private final AppleAuthConfig authConfig;

    @Override
    public OAuthType support() {
        return OAuthType.APPLE;
    }

    @Override
    public String getUrl(String state) {
        return UriComponentsBuilder
                .fromUriString(providerConfig.getAuthorizationUri())
                .queryParam("response_type", "code")
                .queryParam("client_id", authConfig.getClientId())
                .queryParam("redirect_uri", authConfig.getRedirectUri())
                .queryParam("scope","email name")
                .queryParam("response_mode","form_post")
                .queryParam("state",state)
                .toUriString();
    }
}
