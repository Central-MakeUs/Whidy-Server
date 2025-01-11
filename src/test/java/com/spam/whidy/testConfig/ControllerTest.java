package com.spam.whidy.testConfig;

import com.spam.whidy.common.config.auth.AuthArgumentResolver;
import com.spam.whidy.common.config.jwt.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.token.TokenService;
import org.springframework.test.web.servlet.MockMvc;

public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected AuthArgumentResolver authArgumentResolver;

    @MockBean
    protected TokenUtil tokenUtil;

    @MockBean
    protected TokenService tokenService;

}
