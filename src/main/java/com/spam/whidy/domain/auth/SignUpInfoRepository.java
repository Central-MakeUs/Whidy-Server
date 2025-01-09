package com.spam.whidy.domain.auth;

import java.util.Optional;

public interface SignUpInfoRepository {

    void save(String signUpCode, SignUpInfo signUpInfo);
    Optional<SignUpInfo> findByCode(String signUpCode);

}
