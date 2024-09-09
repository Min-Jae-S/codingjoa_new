package com.codingjoa.security.oauth2.service;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.security.service.JwtProvider;
import com.codingjoa.util.CookieUtils;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

	private static final String JWT_COOKIE = "ACCESS_TOKEN";
	private static final long COOKIE_EXPIRE_SECONDS = Duration.ofHours(1l).getSeconds();
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private final JwtProvider jwtProvider;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		log.info("\t > create JWT and issue it as a cookie");
		String jwt = jwtProvider.createJwt(authentication, request);
		CookieUtils.addCookie(request, response, JWT_COOKIE, jwt, COOKIE_EXPIRE_SECONDS);

		String continueUrl = (String) authentication.getDetails();
		continueUrl = UriUtils.resolveContinueUrl(continueUrl, request);
		
		// opation1 : after forwading to view(jsp), alert message and redirect to contineUrl on the client-side
		// opation2 : directly redirect to continueUrl using redirectStrategy
		
		//redirectStrategy.sendRedirect(request, response, continueUrl);
		request.setAttribute("message", MessageUtils.getMessage("success.Login"));
		request.setAttribute("redirectUrl", continueUrl);
		request.getRequestDispatcher("/WEB-INF/views/feedback.jsp").forward(request, response);
	}
}
