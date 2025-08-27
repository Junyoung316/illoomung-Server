package com.reserve.illoomung.core.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Time {

    public static LocalDateTime krTime() {
        return LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    // 1. LocalDate를 문자열로 변환
    public static String localDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(formatter);
    }

    // 4. 문자열을 LocalDate로 변환
    public static LocalDate stringToLocalDate(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(dateStr, formatter);
    }
}
