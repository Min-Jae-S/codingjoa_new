package com.codingjoa.util;

import org.springframework.util.StringUtils;

public class MyNumberUtils {
	
	public static boolean isNaturalNumber(String str) {

		if (!StringUtils.hasText(str)) {
			return false;
		}
		
		char tmp = str.charAt(0);
		if (tmp <= '0' || tmp > '9') {
			return false;
		}
		
		for (int i = 1; i < str.length(); i++) {
			tmp = str.charAt(i);
			if(tmp < '0' || tmp > '9') {
				return false;
			}
		}
		
		return true;
	}
}
