package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.dto.request.auth.register.LocalRegisterRequest;
import com.reserve.illoomung.dto.request.auth.register.SocialRegisterRequest;


public interface RegisterService {
    void localRegister(LocalRegisterRequest request);
    void socialRegister(SocialRegisterRequest request, String socialToken);
    
}
