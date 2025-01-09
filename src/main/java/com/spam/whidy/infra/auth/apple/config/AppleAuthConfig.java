package com.spam.whidy.infra.auth.apple.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "oauth.apple")
public class AppleAuthConfig {

    private String clientId;
    private String teamId;
    private String keyID;
    private String redirectUri;

}