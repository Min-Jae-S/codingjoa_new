package com.codingjoa.service.impl;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.stereotype.Service;

import com.codingjoa.service.UrlValidationService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UrlValidationServiceImpl implements UrlValidationService {

	private static final String[] SCHEMES = {"http", "https"};
	
	@Override
	public boolean validateUrl(HttpServletRequest request, String url) throws MalformedURLException {
		UrlValidator urlValidator = new UrlValidator(SCHEMES, UrlValidator.ALLOW_LOCAL_URLS);
		
		if(!urlValidator.isValid(url)) {
			log.info("\t > invalid url format");
			return false;
		}
		
		String urlHost = new URL(url).getHost();
		String currentHost = request.getServerName();
		log.info("\t > urlHost = {}, currentHost = {}", urlHost, currentHost);
		
		if (!urlHost.equals(currentHost)) {
			log.info("\t > invalid host : {}", urlHost);
			return false;
		}
		
		log.info("\t > valid url");
		return true;
	}

}
