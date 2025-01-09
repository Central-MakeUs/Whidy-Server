package com.spam.whidy.infra.auth.google;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.oauthCodeRequest.AuthCodeRequestUrlProvider;
import com.spam.whidy.infra.auth.google.config.GoogleAuthConfig;
import com.spam.whidy.infra.auth.google.config.GoogleProviderConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class GoogleAuthCodeRequestUrlProvider implements AuthCodeRequestUrlProvider {

    private final GoogleProviderConfig providerConfig;
    private final GoogleAuthConfig authConfig;

    @Override
    public OAuthType support() {
        return OAuthType.GOOGLE;
    }

    @Override
    public String getUrl(String state) {
        return UriComponentsBuilder
                .fromUriString(providerConfig.getAuthorizationUri())
                .queryParam("client_id", authConfig.getClientId())
                .queryParam("redirect_uri", authConfig.getRedirectUri())
                .queryParam("scope", authConfig.getScope())
                .queryParam("response_type", "code")
                .queryParam("state",state)
                .toUriString();
    }
}
