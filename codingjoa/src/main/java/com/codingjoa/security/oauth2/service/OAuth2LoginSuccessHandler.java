package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.enums.OAuth2LoginStatus;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private static final long COOKIE_EXPIRE_SECONDS = Duration.ofHours(6L).getSeconds();
	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/alert-and-redirect.jsp";
	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > principal = {}", authentication.getPrincipal());
		
		log.info("\t > create JWT and issue it as a cookie");
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);

		// CASE 1) after forwading to view(jsp), alert message and redirect to contineUrl on the client-side
		String continueUrl = (String) authentication.getDetails();
		continueUrl = UriUtils.resolveContinueUrl(continueUrl, request);
		request.setAttribute("continueUrl", continueUrl);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		OAuth2LoginStatus loginStatus = principal.getLoginStatus();
		
		String message;
		switch (loginStatus) {
		case NEW:
			message = MessageUtils.getMessage("success.login.oauth2.new");
			break;
			
		case LINKED:
			message = MessageUtils.getMessage("success.login.oauth2.linked");
			break;
			
		case LOGGED_IN:
		default:
			message = MessageUtils.getMessage("success.login.oauth2");
		}
		
		request.setAttribute("message", message);
		
		log.info("\t > forward to 'alert-and-redirect.jsp'");
		request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
		
		// CASE 2) directly redirect to continueUrl using redirectStrategy
		//redirectStrategy.sendRedirect(request, response, continueUrl);
	}
}
