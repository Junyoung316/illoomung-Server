package com.reserve.illoomung.application.auth.register;

import com.reserve.illoomung.core.domain.entity.Account;
import com.reserve.illoomung.core.domain.entity.enums.Role;
import com.reserve.illoomung.dto.request.auth.register.RegisterRequest;

public interface RegisterService {
    Account register(RegisterRequest request, Role role);
    
}
