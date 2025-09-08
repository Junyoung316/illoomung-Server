package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.core.dto.LoginResponse;
import com.reserve.illoomung.dto.request.auth.LocalRegisterLoginRequest;
import com.reserve.illoomung.dto.request.auth.SocialRegisterLoginRequest;

import java.util.Optional;


public interface RegisterService {
    void localRegister(LocalRegisterLoginRequest request);
    
}
