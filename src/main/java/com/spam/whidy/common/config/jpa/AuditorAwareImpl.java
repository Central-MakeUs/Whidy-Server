package com.spam.whidy.common.config.jpa;

import com.spam.whidy.common.config.auth.UserContext;
import org.springframework.data.domain.AuditorAware;
import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        return Optional.of(UserContext.getCurrentUser());
    }
}