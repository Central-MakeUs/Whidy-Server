package com.spam.whidy.infra.auth;

import com.spam.whidy.domain.auth.AuthTokenRepository;
import com.spam.whidy.infra.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenRepositoryImpl implements AuthTokenRepository {

    private final RedisClient redisClient;
    private static final String REFRESH_TOKEN_NAMESPACE = "token:refresh:";
    private static final String BLACKLIST_NAMESPACE = "token:blacklist:";

    @Override
    public void saveRefreshToken(Long userId, String token, Date expireAt) {
        String key = getRefreshTokenKey(userId);
        redisClient.save(key, token, expireAt);
    }

    @Override
    public String getRefreshToken(Long userId) {
        String key = getRefreshTokenKey(userId);
        return (String) redisClient.getData(key);
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        String key = getRefreshTokenKey(userId);
        redisClient.delete(key);
    }

    @Override
    public void addBlackList(String token, Date expireAt){
        String key = getBlacklistTokenKey(token);
        redisClient.save(key, token, expireAt);
    }

    @Override
    public boolean isBlackListToken(String token){
        String key = getBlacklistTokenKey(token);
        return redisClient.getData(key) != null;
    }

    private String getRefreshTokenKey(Long userId){
        return REFRESH_TOKEN_NAMESPACE +userId;
    }

    private String getBlacklistTokenKey(String token){
        return BLACKLIST_NAMESPACE +token;
    }

}
