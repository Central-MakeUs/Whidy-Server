package com.spam.whidy.domain.auth;

import java.util.Optional;

public interface StateRepository {

    void save(String sessionId, String state);
    Optional<String> find(String sessionId);
}
