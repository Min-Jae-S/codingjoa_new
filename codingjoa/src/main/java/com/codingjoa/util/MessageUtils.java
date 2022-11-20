package com.codingjoa.util;

import org.springframework.context.support.MessageSourceAccessor;

public class MessageUtils {
	
	private static MessageSourceAccessor accessor;
	
	public void setMessageSourceAccessor(MessageSourceAccessor accessor) {
		MessageUtils.accessor = accessor;
	}
	
	public static String getMessage(String code) {
		return accessor.getMessage(code);
	}
}
