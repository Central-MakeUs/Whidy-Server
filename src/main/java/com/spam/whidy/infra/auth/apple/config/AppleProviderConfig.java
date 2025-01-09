package com.spam.whidy.infra.auth.apple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oauth.provider.apple")
public class AppleProviderConfig {

    private String authorizationUri;
    private String tokenUri;
    private String userInfoUrl;
    private String userNameAttribute;
    private String serverDomain;

}
