package com.spam.whidy.domain.auth.oauth.oauthCodeRequest;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;


@Component
public class AuthCodeRequestUrlProviderComposite {

    private final Map<OAuthType, AuthCodeRequestUrlProvider> providersByType;

    public AuthCodeRequestUrlProviderComposite(Set<AuthCodeRequestUrlProvider> providers){
        providersByType = providers.stream().collect(toMap(AuthCodeRequestUrlProvider::support, Function.identity()));
    }

    public String getOauthCodeRequestUrl(OAuthType type, String state){
        return providersByType.get(type).getUrl(state);
    }
}
