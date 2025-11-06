package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.dto.request.auth.LocalRegisterRequest;

public interface RegisterService {
    void localRegister(LocalRegisterRequest request);
    
}
