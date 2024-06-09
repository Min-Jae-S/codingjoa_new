package com.codingjoa.interceptor;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.entity.Member;
import com.codingjoa.response.ErrorResponse;
import com.codingjoa.security.dto.UserDetailsDto;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class PasswordConfirmInterceptor implements HandlerInterceptor {
	
	private final RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > URI = {} '{}'", request.getMethod(), getFullURI(request));
		
		if (!checkPasswordConfirm()) {
			String message =  MessageUtils.getMessage("error.NotConfirmPassword");
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				responseJSON(request, response, message);
			} else {
				responseHTML(request, response, message);
			}
			return false;
		}
		return true;
	}
	
//	private boolean checkPasswordConfirm(HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		Boolean passwordConfirm = (Boolean) session.getAttribute("PASSWORD_CONFIRM");
//		log.info("\t > PASSWORD_CONFIRM = {}", passwordConfirm);
//		
//		return (passwordConfirm == null) ? false : passwordConfirm;
//	}

	private boolean checkPasswordConfirm() {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > authentication = {}", authentication);
		if (authentication == null) {
			return false;
		}
		
		Object principal = authentication.getPrincipal();
		log.info("\t > principal = {}", principal);
		if (!(principal instanceof UserDetailsDto)) { // String, "anonymousUser"
			return false;
		}
		
		Member currentMember = ((UserDetailsDto) authentication.getPrincipal()).getMember();
		String passwordConfirm = redisService.findValueByKey(currentMember.getMemberId());
		log.info("\t > passwordConfirm = {}", passwordConfirm);
		
		return "PASSWORD_CONFIRM".equals(passwordConfirm);
	}
	
	private void responseJSON(HttpServletRequest request, HttpServletResponse response, String message)
			throws JsonProcessingException, IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.timeZone(TimeZone.getTimeZone("Asia/Seoul"))
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.build();
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.FORBIDDEN)
				.message(message)
				.build();
		log.info("\t > {}", errorResponse);
		
		log.info("\t > respond with errorResponse in JSON format");
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse)); // \n --> \\n
		response.getWriter().close();
	}
	
	private void responseHTML(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		String script = "<script>";
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
		script += "alert('" + message + "');";
		script += "location.href='" + request.getContextPath() + "/member/account/confirmPassword';";
		script += "</script>";
		
		log.info("\t > respond with HTML format");
		response.getWriter().write(script);
		response.getWriter().close();
	}
	
	private String getFullURI(HttpServletRequest request) {
		StringBuilder requestURI = 
				new StringBuilder(URLDecoder.decode(request.getRequestURI(), StandardCharsets.UTF_8));
	    String queryString = request.getQueryString();
	    
	    if (queryString == null) {
	        return requestURI.toString();
	    } else {
	    	return requestURI.append('?')
	    			.append(URLDecoder.decode(queryString, StandardCharsets.UTF_8)).toString();
	    }
	}
}