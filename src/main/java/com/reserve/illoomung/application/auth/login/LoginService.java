package com.reserve.illoomung.application.auth.login;

import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;

public interface LoginService {
    void localLogin(LocalRegisterLoginRequest request);
    void socialLogin(SocialRegisterLoginRequest request, String socialToken);
}
