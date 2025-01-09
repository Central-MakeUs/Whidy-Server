package com.spam.whidy.infra.auth;

import com.spam.whidy.domain.auth.StateRepository;
import com.spam.whidy.infra.RedisClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class StateRepositoryImpl implements StateRepository {

    private final RedisClient redisClient;
    private static final String SIGN_UP_INFO_NAMESPACE = "oauth-state:";
    @Value("${application.property.oauth-state.ttl-minute}")
    private int stateTTL;

    @Override
    public void save(String sessionId, String state) {
        String key = SIGN_UP_INFO_NAMESPACE + sessionId;
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(stateTTL);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        redisClient.save(key, state, date);
    }

    @Override
    public Optional<String> find(String sessionId) {
        String key = SIGN_UP_INFO_NAMESPACE + sessionId;
        return Optional.of((String)redisClient.getData(key));
    }
}
