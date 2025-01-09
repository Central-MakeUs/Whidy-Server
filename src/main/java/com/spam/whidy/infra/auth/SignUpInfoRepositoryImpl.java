package com.spam.whidy.infra.auth;

import com.spam.whidy.domain.auth.SignUpInfoRepository;
import com.spam.whidy.domain.auth.SignUpInfo;
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
public class SignUpInfoRepositoryImpl implements SignUpInfoRepository {

    private final RedisClient redisClient;
    private static final String SIGN_UP_INFO_NAMESPACE = "signup-code:";
    @Value("${application.property.signup-info.ttl-minute}")
    private int signUpInfoTTL;

    @Override
    public void save(String signUpCode, SignUpInfo signUpInfo) {
        String key = SIGN_UP_INFO_NAMESPACE + signUpCode;
        LocalDateTime localDateTime = LocalDateTime.now().plusMinutes(signUpInfoTTL);
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        redisClient.save(key, signUpInfo, date);
    }

    @Override
    public Optional<SignUpInfo> findByCode(String signUpCode) {
        String key = SIGN_UP_INFO_NAMESPACE + signUpCode;
        return Optional.ofNullable((SignUpInfo) redisClient.getData(key));
    }
}
