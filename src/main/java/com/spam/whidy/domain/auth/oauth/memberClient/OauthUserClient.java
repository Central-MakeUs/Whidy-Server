package com.spam.whidy.domain.auth.oauth.memberClient;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.User;

public interface OauthUserClient {

    OAuthType support();
    User findUser(String code);
}
