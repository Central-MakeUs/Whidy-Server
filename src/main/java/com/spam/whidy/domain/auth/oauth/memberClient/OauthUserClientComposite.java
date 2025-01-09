package com.spam.whidy.domain.auth.oauth.memberClient;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class OauthUserClientComposite {

    private final Map<OAuthType, OauthUserClient> clientsByType;

    public OauthUserClientComposite(List<OauthUserClient> clients){
        clientsByType = clients.stream().collect(Collectors.toMap(OauthUserClient::support, Function.identity()));
    }

    public User findOauthUser(OAuthType type, String code){
        return clientsByType.get(type).findUser(code);
    }
}
