package com.reserve.illoomung.application.verification.oauth.kakao;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.reserve.illoomung.core.exception.InvalidKakaoTokenException;
import com.reserve.illoomung.core.exception.KakaoApiClientException;
import com.reserve.illoomung.core.exception.KakaoApiServerException;
import com.reserve.illoomung.dto.response.verification.oauth.kakao.KakaoUserInfoResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoServiceImpl implements KakaoService {

    private final WebClient kakaoWebClient;

    @Override
    public KakaoUserInfoResponse getKakaoUserInfo(String accessToken) {
        String tokenInfo = kakaoWebClient.get() // 토큰 정보 요청(토큰 정보 검증, 실패 시 401 예외 발생)
            .uri("/v1/user/access_token_info")
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
            .retrieve()
            // 1. 401 에러를 먼저 처리합니다.
            .onStatus(httpStatus -> httpStatus.value() == 401,
                clientResponse -> Mono.error(new InvalidKakaoTokenException("카카오 토큰이 유효하지 않습니다."))
            )
            // 2. 401이 아닌 다른 4xx 에러들을 처리합니다.
            .onStatus(httpStatus -> httpStatus.is4xxClientError() && httpStatus.value() != 401,
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(
                        new KakaoApiClientException(
                            "카카오 API 클라이언트 오류: " + errorBody)
                    ))
            )
            // 3. 5xx 에러들을 처리합니다.
            .onStatus(httpStatus -> httpStatus.is5xxServerError(),
                clientResponse -> clientResponse.bodyToMono(String.class)
                    .flatMap(errorBody -> Mono.error(
                        new KakaoApiServerException(
                            "카카오 API 서버 오류: " + errorBody)
                    ))
            )
            .bodyToMono(String.class) // 모든 에러 처리를 통과한 경우에만 body를 가져옵니다.
            .block();
        
        log.info("[회원가입] KAKAO 토큰 정보: {}", tokenInfo);

        if (tokenInfo != null && !tokenInfo.isEmpty()) {
            Mono<KakaoUserInfoResponse> jsonResponse = kakaoWebClient.get() // 소셜 정보 요청
                .uri("/v2/user/me")
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .bodyToMono(KakaoUserInfoResponse.class);

            KakaoUserInfoResponse jsonResponseBody = jsonResponse.block();

            String jsonResponseInfo = String.valueOf(jsonResponseBody);
            log.debug(jsonResponseInfo);

            return jsonResponseBody;
        }
        return null; // 토큰 정보가 유효하지 않거나 비어있는 경우
    }
}
