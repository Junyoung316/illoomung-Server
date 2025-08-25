package com.reserve.illoomung.core.exception;

public class NoAuthorizationHeaderException extends RuntimeException {
    public NoAuthorizationHeaderException(String message) {
        super(message);
    }
    
}
