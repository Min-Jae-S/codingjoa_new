package com.codingjoa.interceptor;

import java.io.IOException;
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
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.HttpUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordResetKeyInterceptor implements HandlerInterceptor {

	private final RedisService redisService;
	private final ObjectMapper objectMapper;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		
		response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());

		String key = request.getParameter("key");
		if (!checkPasswordResetKey(key)) {
			String message =  MessageUtils.getMessage("error.NotPasswordResetKey");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				responseJSON(request, response, message);
			} else {
				response.setContentType(MediaType.TEXT_HTML_VALUE);
				responseHTML(request, response, message);
			}
			return false;
		}
		
		return true;
	}
	
	private boolean checkPasswordResetKey(String key) {
		return StringUtils.isEmpty(key) ? false : redisService.hasKey(key);
	}
	
	private void responseJSON(HttpServletRequest request, HttpServletResponse response, String message)
			throws JsonProcessingException, IOException {
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.message(message)
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		String jsonResponse = objectMapper.writeValueAsString(errorResponse);
		response.getWriter().write(jsonResponse); // \n --> \\n
		response.getWriter().close();
	}
	
	private void responseHTML(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
		String script = "<script>";
		script += "alert('" + message + "');";
		script += "location.href='" + request.getContextPath() + "/member/findPassowrd';";
		script += "</script>";
		
		log.info("\t > respond with errorsResponse in HTML format");
		response.getWriter().write(script);
		response.getWriter().close();
	}
}