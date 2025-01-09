package com.spam.whidy.domain.auth.oauth.oauthCodeRequest;

import com.spam.whidy.domain.auth.oauth.OAuthType;

public interface AuthCodeRequestUrlProvider {

    OAuthType support();
    String getUrl(String state);
}
