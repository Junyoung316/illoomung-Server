package com.reserve.illoomung.core.dto;

import java.time.Instant;

public record TokenIatExp(String token, Instant issuedAt, Instant expiresAt) {
}
