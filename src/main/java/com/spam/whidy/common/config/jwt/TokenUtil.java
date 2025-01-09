package com.spam.whidy.common.config.jwt;

import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TokenUtil {

    @Value("${application.secret-key}")
    private String secretKey;
    @Value("${application.expiration-time.access-token}")
    private Long accessTokenTime;
    @Value("${application.expiration-time.refresh-token}")
    private Long refreshTokenTime;

    public String generateAccessToken(Long userId){
        return JWTUtil.generateToken(userId, secretKey,accessTokenTime);
    }

    public String generateRefreshToken(Long userId){
        return JWTUtil.generateToken(userId, secretKey,refreshTokenTime);
    }

    public Long getUserIdFromRequest(HttpServletRequest request) {
        String token = resolveTokenFromRequest(request);
        return JWTUtil.getUserIdFromToken(token, secretKey);
    }

    public String resolveTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new BadRequestException(ExceptionType.TOKEN_NOT_EXIST);
        }
        return authorizationHeader.substring(7);
    }

    public boolean isTokenValid(String token) {
        try {
            JWTUtil.validateToken(token, secretKey);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Long getUserIdFromToken(String token) {
        return JWTUtil.getUserIdFromToken(token, secretKey);
    }

    public Date getExpireDateFromToken(String token) {
        return JWTUtil.getExpireDateFromToken(token, secretKey);
    }
}
