package com.codingjoa.service;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

public interface UrlValidationService {
	
	boolean validateUrl(HttpServletRequest request, String url) throws MalformedURLException;
	
}
