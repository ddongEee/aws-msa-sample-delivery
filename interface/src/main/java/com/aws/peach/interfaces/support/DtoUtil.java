package com.aws.peach.interfaces.support;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class DtoUtil {
    public static String formatTimestamp(Instant timestamp) {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }
}
