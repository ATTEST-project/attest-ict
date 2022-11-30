package com.attest.ict.custom.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateFormatter {

    public static final Logger LOG = LoggerFactory.getLogger(DateFormatter.class);

    public static String getCurrentDateTimeFormatted() {
        String dateTimePattern = "yyyyMMdd_HHmmss";
        DateTimeFormatter dataTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        LocalDateTime ldt = LocalDateTime.now();
        String dateTimeToStr = dataTimeFormatter.format(ldt);
        LOG.debug("Current DateTime formatted: {} ", dateTimeToStr);
        return dateTimeToStr;
    }
}
