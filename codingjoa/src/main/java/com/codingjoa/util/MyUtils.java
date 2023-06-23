package com.codingjoa.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MyUtils {

	public static boolean isPageNumber(String str) {
		try {
			int pageNumber = Integer.parseInt(str);
			return pageNumber > 0;
		} catch (Exception e) {
			log.info("\t > {}", e.getMessage());
			return false;
		}
		
//		char tmp = str.charAt(0);
//		if (tmp <= '0' || tmp > '9') {
//			return false;
//		}
//		
//		for (int i = 1; i < str.length(); i++) {
//			tmp = str.charAt(i);
//			if(tmp < '0' || tmp > '9') {
//				return false;
//			}
//		}
//		
	}
}
