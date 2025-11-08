package com.reserve.illoomung.core.exception;

import com.reserve.illoomung.core.domain.entity.Account;

public class LoginFailException extends RuntimeException {
    private final Account account;
    private final String reason;

    public LoginFailException(Account accountId, String reason) {
        super(reason);
        this.account = accountId;
        this.reason = reason;
    }

    public Account getAccount() { return account; }
    public String getReason() { return reason; }
}