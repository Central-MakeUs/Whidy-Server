package com.spam.whidy.infra.config;

import com.spam.whidy.infra.auth.apple.AppleApiClient;
import com.spam.whidy.infra.auth.google.GoogleApiClient;
import com.spam.whidy.infra.auth.google.GoogleAuthCodeRequestUrlProvider;
import com.spam.whidy.infra.auth.kakao.KakaoApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class HttpInterfaceConfig {

    @Bean
    public KakaoApiClient kakaoApiClient(){
        return createHttpInterface(KakaoApiClient.class);
    }
    @Bean
    public AppleApiClient appleApiClient(){
        return createHttpInterface(AppleApiClient.class);
    }
    @Bean
    public GoogleApiClient googleApiClient(){
        return createHttpInterface(GoogleApiClient.class);
    }

    private <T> T createHttpInterface(Class<T> clazz){
        WebClient webClient = WebClient.create();
        HttpServiceProxyFactory build = HttpServiceProxyFactory
                .builder().exchangeAdapter(WebClientAdapter.create(webClient)).build();
        return build.createClient(clazz);
    }

}
