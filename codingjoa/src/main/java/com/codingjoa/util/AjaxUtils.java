package com.codingjoa.util;

import javax.servlet.http.HttpServletRequest;

public class AjaxUtils {
	
	public static final String AJAX_HEADER = "XMLHttpRequest";

	public static boolean isAjaxRequest(HttpServletRequest request) {
		return AJAX_HEADER.equals(request.getHeader("x-requested-with"));
	}
}
