package com.spam.whidy.infra.auth.apple;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.spam.whidy.domain.auth.oauth.OAuthType;
import com.spam.whidy.domain.auth.oauth.memberClient.OauthUserClient;
import com.spam.whidy.domain.user.User;
import com.spam.whidy.infra.auth.apple.config.AppleAuthConfig;
import com.spam.whidy.infra.auth.apple.config.AppleProviderConfig;
import com.spam.whidy.infra.auth.apple.response.AppleToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class AppleUserClient implements OauthUserClient {

    private final AppleApiClient appleApiClient;
    private final AppleAuthConfig authConfig;
    private final AppleProviderConfig appleProviderConfig;
    private final PrivateKey privateKey;

    @Override
    public OAuthType support() {
        return OAuthType.APPLE;
    }

    @Override
    public User findUser(String code) {
        AppleToken token = appleApiClient.fetchToken(makeRequestTokenParam(code));
        return extractUserFromAppleToken(token);
    }

    private User extractUserFromAppleToken(AppleToken token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token.getIdToken());
            String userId = signedJWT.getJWTClaimsSet().getSubject();
            String email = (String) signedJWT.getJWTClaimsSet().getClaim("email");
            return User.of(email, OAuthType.APPLE, userId);
        }catch (Exception e){
            throw new RuntimeException("애플 Oauth : Apple Token 으로부터 User 정보 추출 실패, " + e.getMessage());
        }
    }

    private MultiValueMap<String, String> makeRequestTokenParam(String code) {
        MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
        param.add("client_secret", generateClientSecret());
        param.add("grant_type", "authorization_code");
        param.add("code", code);
        param.add("client_id", authConfig.getClientId());
        param.add("redirect_uri", authConfig.getRedirectUri());
        return param;
    }

    private String generateClientSecret() {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("alg", "ES256");
        jwtHeader.put("kid", authConfig.getKeyID());

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(authConfig.getTeamId())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(expirationDate)
                .setAudience(appleProviderConfig.getServerDomain())
                .setSubject(authConfig.getClientId())
                .signWith(privateKey, SignatureAlgorithm.ES256)
                .compact();
    }
}
