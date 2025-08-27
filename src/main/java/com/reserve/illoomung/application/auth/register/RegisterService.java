package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;


public interface RegisterService {
    void localRegister(LocalRegisterLoginRequest request);
    void socialRegister(SocialRegisterLoginRequest request, String socialToken);
    
}
