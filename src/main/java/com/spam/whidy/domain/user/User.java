package com.spam.whidy.domain.user;

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
    @Embedded
    @Builder.Default
    private ProfileImage profileImage = new ProfileImage();
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OAuthType oauthType;
    @Column(nullable = false)
    private String oauthId;
    @Builder.Default
    @Column(nullable = false)
    private LocalDateTime joinDateTime = LocalDateTime.now();
    @Builder.Default
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

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

    public void updateRole(Role role){
        this.role = role;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateProfileKey(String profileKey) {
        getProfileImage().profileImageKey = profileKey;
        getProfileImage().profileImageUrl = null;
        getProfileImage().urlExpireDateTime = null;
    }

    public void updateProfileImageUrl(String url, LocalDateTime expirationDateTime){
        getProfileImage().profileImageUrl = url;
        getProfileImage().urlExpireDateTime = expirationDateTime;
    }

    public String getProfileImageKey(){
        return getProfileImage().profileImageKey;
    }

    public String getProfileImageUrl(){
        return getProfileImage().profileImageUrl;
    }

    public boolean isProfileImageUrlValid(){
        return getProfileImage().profileImageUrl != null
                && getProfileImage().urlExpireDateTime.isAfter(LocalDateTime.now());
    }

    public boolean hasProfileImage() {
        return getProfileImage().profileImageKey != null;
    }

    public ProfileImage getProfileImage(){
        if(profileImage == null){
            profileImage = new ProfileImage();
        }
        return profileImage;
    }

}
