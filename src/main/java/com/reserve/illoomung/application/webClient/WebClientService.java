package com.reserve.illoomung.application.webClient;

import com.reserve.illoomung.dto.webClient.KakaoAddressResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient.Builder webClientBuilder;

    @Value(value = "${kakao.api.key.rest}")
    private String KAKAORESTAPIKEY;

    public KakaoAddressResponse kakaoGetBCode(String address) {
            WebClient webClient = webClientBuilder.baseUrl("https://dapi.kakao.com").build();
            Mono<KakaoAddressResponse> response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v2/local/search/address.json")
                            .queryParam("analyze_type", "similar")
                            .queryParam("query", address)
                            .queryParam("page", 1)
                            .queryParam("size", 1)
                            .build())
                    .header("Authorization", "KakaoAK " + KAKAORESTAPIKEY)
                    .retrieve()
                    .bodyToMono(KakaoAddressResponse.class);
            return response.block(); // 동기 처리, 비동기라면 .subscribe() 사용
    }
}
