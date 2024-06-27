package com.codingjoa.service;

import javax.servlet.http.HttpServletRequest;

public interface UrlValidationService {
	
	boolean validateUrl(HttpServletRequest request, String url);
	
}
