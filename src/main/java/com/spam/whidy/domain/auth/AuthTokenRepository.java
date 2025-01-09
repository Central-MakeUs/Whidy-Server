package com.spam.whidy.domain.auth;

import java.util.Date;

public interface AuthTokenRepository {
    void saveRefreshToken(Long userId, String token, Date timeout);
    String getRefreshToken(Long userId);
    void deleteRefreshToken(Long userId);
    void addBlackList(String token, Date timeOut);
    boolean isBlackListToken(String token);
}
