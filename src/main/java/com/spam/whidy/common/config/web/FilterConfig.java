package com.spam.whidy.common.config.web;

import com.spam.whidy.common.config.jwt.JWTFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilter(JWTFilter jwtFilter) {
        FilterRegistrationBean<JWTFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(jwtFilter);
        filterRegistrationBean.addUrlPatterns("/api/*");
        filterRegistrationBean.addUrlPatterns("/sign-out");
        filterRegistrationBean.addUrlPatterns("/refresh-token");
        return filterRegistrationBean;
    }
}
