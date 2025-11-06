package com.reserve.illoomung.application.auth.login;

import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.dto.request.auth.LocalLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;

public interface LoginService {
    LoginResponse localLogin(LocalLoginRequest request);
    void socialLogin(SocialRegisterLoginRequest request, Long accountId);
}
