package com.codingjoa.security.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
	
	@SuppressWarnings("unused")
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String continueUrl = request.getParameter("continue");
		continueUrl = UriUtils.resolveContinueUrl(continueUrl, request);
		
		SuccessResponse successResponse = SuccessResponse.builder()
				.status(HttpStatus.OK)
				.messageByCode("success.Logout")
				.data(Map.of("redirectUrl", continueUrl))
				.build();
		
		//redirectStrategy.sendRedirect(request, response, continueUrl);
		request.setAttribute("successResponse", successResponse);
		request.getRequestDispatcher("/WEB-INF/views/feedback/success.jsp").forward(request, response);
	}

}
