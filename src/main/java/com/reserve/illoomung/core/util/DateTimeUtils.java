package com.reserve.illoomung.core.util;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public final class DateTimeUtils {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Instant now() {
        return Instant.now();
    }


    // String <-> Instant 변환
    public static String instantToString(Instant instant) {
        if(instant == null) {
            return null;
        }
        return instant.toString();
    }

    public static Instant  stringToInstant(String instantStr){
        if(instantStr == null || instantStr.isBlank()) {
            return null;
        }
        return Instant.parse(instantStr);
    }

    // Date <-> Instant 변환
    public static Date instantToDate(Instant instant) {
        if(instant == null) {
            return null;
        }
        return Date.from(instant);
    }

    public static Instant dateToInstant(Date date) {
        if(date == null) {
            return null;
        }
        return date.toInstant();
    }

    // Long <-> Instant 변환 (초 단위)
    public static Long instantToEpochSeconds(Instant instant) {
        if(instant == null) {
            return null;
        }
        return instant.getEpochSecond();
    }

    public static Instant epochSecondsToInstant(Long epochSeconds) {
        if(epochSeconds == null) {
            return null;
        }
        return Instant.ofEpochSecond(epochSeconds);
    }
}
