package com.codingjoa.interceptor;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.HttpUtils;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordResetInterceptor implements HandlerInterceptor {

	private static final String FORWARD_PATH = "/WEB-INF/views/feedback/alert-and-redirect.jsp";
	private final RedisService redisService;
	private final ObjectMapper objectMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getRequestLine(request));
		
		String key = request.getParameter("key");
		
		if (!isPasswordResetKey(key)) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			
			if (isRestController(handler)) {
				respondJson(response);
			} else {
				respondJsp(request, response);
			}
			
			return false;
		}
		
		return true;
	}
	
	private boolean isPasswordResetKey(String key) {
		return StringUtils.isNotEmpty(key) && redisService.hasKey(key);
	}
	
	private boolean isRestController(Object handler) {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			return handlerMethod.getBeanType().isAnnotationPresent(RestController.class);
		}
		
		return false;
	}
	
	private void respondJson(HttpServletResponse response) throws Exception {
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.messageByCode("error.reset-passoword.NotValidKey")
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.getWriter().write(jsonResponse); // \n --> \\n
		response.getWriter().close();
	}
	
	private void respondJsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String message = MessageUtils.getMessage("error.reset-passoword.NotValidKey");
		//message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
		
		request.setAttribute("message", message);
		request.setAttribute("continueUrl", request.getContextPath() + "/password/find");
		
		log.info("\t > forward to 'alert-and-redirect.jsp'");
		request.getRequestDispatcher(FORWARD_PATH).forward(request, response);
	}

}