package com.spam.whidy.application.user;

import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.user.Role;
import com.spam.whidy.domain.user.User;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserInitializer {

    private final UserFinder userFinder;
    private final UserService userService;

    @Value("${application.property.super-admin.name}")
    private String name;
    @Value("${application.property.super-admin.oauth-id}")
    private String oauthId;
    @Value("${application.property.super-admin.oauth-type}")
    private String oauthType;
    @Value("${application.property.super-admin.email}")
    private String email;

    @PostConstruct
    public void initSuperAdmin(){
        if(userFinder.findByEmail(email).isEmpty()){
            saveSuperAdmin();
        }
    }

    private void saveSuperAdmin() {
        User user = User.builder()
                .email(email)
                .name(name)
                .oauthType(OAuthType.valueOf(oauthType))
                .oauthId(oauthId)
                .role(Role.SUPER_ADMIN)
                .build();
        userService.save(user);
    }


}
