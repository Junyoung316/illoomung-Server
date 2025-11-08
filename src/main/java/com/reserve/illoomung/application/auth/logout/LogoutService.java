package com.reserve.illoomung.application.auth.logout;

public interface LogoutService {
    void logout(String accessToken, String refreshToken);
}
