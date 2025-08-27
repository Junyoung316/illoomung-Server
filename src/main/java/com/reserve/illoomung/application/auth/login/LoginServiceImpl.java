package com.reserve.illoomung.application.auth.login;

import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;

public class LoginServiceImpl implements LoginService {

    @Override
    public void localLogin(LocalRegisterLoginRequest request){
        // TODO: 로컬 로그인을 위해 이메일 해시 생성 후 해시를 이용해 데이터를 찾아 비밀번호 비교 후 맞으면 로그인 틀리면 예외 처리

    }

    @Override
    public void socialLogin(SocialRegisterLoginRequest request, String socialToken) {
        // TODO: regiser 서비스에서 이미 가입된 사용자를 로그인 처리 로직 구현
    }

}
