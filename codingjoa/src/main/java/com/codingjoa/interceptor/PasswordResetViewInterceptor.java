package com.codingjoa.interceptor;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.service.RedisService;
import com.codingjoa.util.RequestUtils;
import com.codingjoa.util.MessageUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordResetViewInterceptor implements HandlerInterceptor {

	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/alert-and-redirect.jsp";
	private final RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > {}", RequestUtils.getRequestLine(request));
		
		String token = request.getParameter("token");
		
		if (!StringUtils.hasText(token) || !redisService.hasKey(token)) {
			log.info("\t > invalid or missing token");
			
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			
			request.setAttribute("message", MessageUtils.getMessage("error.reset-password.notValidToken"));
			request.setAttribute("continueUrl", request.getContextPath() + "/password/find");
			
			log.info("\t > forward to 'alert-and-redirect.jsp'");
			request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
			
			return false;
		}
		
		return true;
	}

}