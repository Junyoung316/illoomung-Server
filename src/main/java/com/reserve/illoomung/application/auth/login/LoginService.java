package com.reserve.illoomung.application.auth.login;

import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;

public interface LoginService {
    LoginResponse localLogin(LocalRegisterLoginRequest request);
    void socialLogin(SocialRegisterLoginRequest request, Long accountId);
}
