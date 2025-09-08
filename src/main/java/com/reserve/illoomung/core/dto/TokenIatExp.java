package com.reserve.illoomung.core.dto;

import java.util.Date;

public record TokenIatExp(String token, Date issuedAt, Date expiresAt) {
}
