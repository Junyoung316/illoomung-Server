package com.reserve.illoomung.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    
    @Bean
    @Qualifier("kakaoWebClient")
    public WebClient kakaoWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://kapi.kakao.com")
                .build();
    }

    @Bean
    @Qualifier("naverWebClient")
    public WebClient naverWebClient(WebClient.Builder builder) {
        return builder
                .baseUrl("https://openapi.naver.com")
                .build();
    }
}
