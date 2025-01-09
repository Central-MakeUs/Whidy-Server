package com.spam.whidy.application.auth.token;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import com.spam.whidy.common.config.jwt.TokenUtil;
import com.spam.whidy.domain.auth.AuthToken;
import com.spam.whidy.domain.auth.AuthTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class AuthTokenService {

    private final TokenUtil tokenUtil;
    private final AuthTokenRepository authTokenRepository;

    public AuthToken createAuthToken(Long userId) {
        String token = tokenUtil.generateAccessToken(userId);
        String refreshToken = tokenUtil.generateRefreshToken(userId);
        saveRefreshToken(userId, refreshToken);
        return new AuthToken(token, refreshToken);
    }

    public AuthToken refreshToken(HttpServletRequest request) {
        Long userId = tokenUtil.getUserIdFromRequest(request);
        String clientToken = tokenUtil.resolveTokenFromRequest(request);
        String serverToken = authTokenRepository.getRefreshToken(userId);
        if (!clientToken.equals(serverToken)) {
            throw new BadRequestException(ExceptionType.TOKEN_NOT_VALID);
        }
        return createAuthToken(userId);
    }

    public void makeTokenInvalid(AuthToken token) {
        Long userId = tokenUtil.getUserIdFromToken(token.getRefreshToken());
        authTokenRepository.deleteRefreshToken(userId);
        Date accessTokenExpirationDate = tokenUtil.getExpireDateFromToken(token.getAccessToken());
        authTokenRepository.addBlackList(token.getAccessToken(), accessTokenExpirationDate);
    }

    public boolean isTokenValid(String token) {
        return tokenUtil.isTokenValid(token) && !authTokenRepository.isBlackListToken(token);
    }

    private void saveRefreshToken(Long userId, String token) {
        Date expireAt = tokenUtil.getExpireDateFromToken(token);
        authTokenRepository.saveRefreshToken(userId, token, expireAt);
    }

}