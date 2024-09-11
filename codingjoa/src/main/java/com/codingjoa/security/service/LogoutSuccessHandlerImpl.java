package com.codingjoa.security.service;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {
	
	private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		
		String continueUrl = request.getParameter("continue");
		continueUrl = UriUtils.resolveContinueUrl(continueUrl, request);
		
		request.setAttribute("message", MessageUtils.getMessage("success.Logout"));
		request.setAttribute("continueUrl", continueUrl);
		
		log.info("\t > forward to feedback.jsp");
		request.getRequestDispatcher("/WEB-INF/views/feedback.jsp").forward(request, response);
		//redirectStrategy.sendRedirect(request, response, continueUrl);
	}

}
