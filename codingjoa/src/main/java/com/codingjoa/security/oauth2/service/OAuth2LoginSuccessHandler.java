package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.time.Duration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.enums.OAuth2LoginStatus;
import com.codingjoa.security.dto.JwtResponseDto;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.util.UriUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private static final long COOKIE_EXPIRE_SECONDS = Duration.ofHours(6L).getSeconds();
	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/oauth2-success.jsp";
	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > principal = {}", authentication.getPrincipal());
		
		log.info("\t > create JWT and issue it via cookie and JSON body");
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);
		
		String continueUrl = (String) authentication.getDetails();
		continueUrl = UriUtils.resolveContinueUrl(continueUrl, request);
		request.setAttribute("continueUrl", continueUrl);
		
		PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
		OAuth2LoginStatus loginStatus = principal.getLoginStatus();
		log.info("\t > loginStatus = {}", loginStatus);

		String code;
		switch (loginStatus) {
		case NEW:
			code = "success.login.oauth2.new";
			break;
			
		case LINKED:
			code = "success.login.oauth2.linked";
			break;
			
		case LOGGED_IN:
		default:
			code = "success.login.oauth2";
		}
		
		JwtResponseDto jwtResponse = JwtResponseDto.builder()
				.accessToken(jwt)
				.build();
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode(code)
				.data(jwtResponse)
				.build();
		request.setAttribute("response", successResponse);
		
		log.info("\t > forward to 'oauth2-success.jsp'");
		request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
	}
}
