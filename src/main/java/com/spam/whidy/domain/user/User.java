package com.spam.whidy.domain.user;

import com.spam.whidy.domain.TimeBaseEntity;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;
    @Column(nullable = false)
    private String oauthId;
    @Column(nullable = false)
    private LocalDateTime joinDateTime;

    @PrePersist
    protected void onCreate() {
        this.joinDateTime = LocalDateTime.now();
    }

    public void update(String name){
        this.name = name;
    }

    public static User of(String email, String name, OAuthType oauthType, String oauthId){
        User user = new User();
        user.name = name;
        user.email = email;
        user.oauthType = oauthType;
        user.oauthId = oauthId;
        return user;
    }

    public static User of(String email, OAuthType oauthType, String oauthId){
        User user = new User();
        user.email = email;
        user.oauthType = oauthType;
        user.oauthId = oauthId;
        return user;
    }

}
