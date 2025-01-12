package com.spam.whidy.common.config.jwt;


import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.common.config.auth.CustomUserDetails;
import com.spam.whidy.common.exception.BadRequestException;
import com.spam.whidy.common.exception.ExceptionType;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final TokenUtil tokenUtil;
    private final AuthTokenService authTokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws IOException {
        try {
            String token =  tokenUtil.resolveTokenFromRequest(request);
            checkTokenValid(token);

            Long userId = tokenUtil.getUserIdFromRequest(request);
            saveUserContext(userId);
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("JWT Auth fail. ",e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Invalid or expired token.");
        }
    }

    private void saveUserContext(Long userId) {
        UserDetails userDetails = CustomUserDetails.of(userId);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public void checkTokenValid(String token){
        if(!authTokenService.isTokenValid(token)){
            throw new BadRequestException(ExceptionType.TOKEN_NOT_VALID);
        }
    }
}
