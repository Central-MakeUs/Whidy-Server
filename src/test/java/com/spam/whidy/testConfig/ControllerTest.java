package com.spam.whidy.testConfig;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spam.whidy.application.auth.AuthService;
import com.spam.whidy.application.auth.token.AuthTokenService;
import com.spam.whidy.common.config.auth.AuthArgumentResolver;
import com.spam.whidy.common.config.jwt.JWTFilter;
import com.spam.whidy.common.config.jwt.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTest {

    @Autowired protected MockMvc mockMvc;
    @Autowired protected ObjectMapper objectMapper;
    @Autowired protected AuthArgumentResolver authArgumentResolver;

    @MockBean protected TokenUtil tokenUtil;
    @MockBean protected JWTFilter jwtFilter;
    @MockBean protected AuthService authService;
    @MockBean protected AuthTokenService authTokenService;

}
