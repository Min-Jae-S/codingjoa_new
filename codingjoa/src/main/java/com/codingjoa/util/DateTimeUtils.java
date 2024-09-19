package com.codingjoa.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtils {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd. HH:mm");
	
	public static String format(LocalDateTime dateTime) {
		return (dateTime == null) ? null : dateTime.format(FORMATTER);
	}
}
