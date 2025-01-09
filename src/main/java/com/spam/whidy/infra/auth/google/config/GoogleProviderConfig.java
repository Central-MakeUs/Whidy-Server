package com.spam.whidy.infra.auth.google.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oauth.provider.google")
public class GoogleProviderConfig {

    private String authorizationUri;
    private String tokenUri;
    private String userInfoUrl;
    private String userNameAttribute;

}
