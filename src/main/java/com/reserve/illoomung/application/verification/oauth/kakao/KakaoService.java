package com.reserve.illoomung.application.verification.oauth.kakao;

import com.reserve.illoomung.dto.response.verification.oauth.kakao.KakaoUserInfoResponse;

public interface KakaoService {
    KakaoUserInfoResponse getKakaoUserInfo(String accessToken);
}
