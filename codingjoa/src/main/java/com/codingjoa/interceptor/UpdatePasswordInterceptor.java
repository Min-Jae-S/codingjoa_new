package com.codingjoa.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
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
public class UpdatePasswordInterceptor implements HandlerInterceptor {
	
	private final RedisService redisService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());
		
		if (!passwordCheck()) {
			String message =  MessageUtils.getMessage("error.NotCheckPassword");
			log.info("\t > original message = {}", message);
			
			message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
			log.info("\t > processed message = {}", message);
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				log.info("\t > {} '{}' --> responseJSON", request.getMethod(), request.getRequestURI());
				responseJSON(request, response, message);
			} else {
				log.info("\t > {} '{}' --> responseHTML", request.getMethod(), request.getRequestURI());
				responseHTML(request, response, message);
			}
			return false;
		}
		return true;
	}
	
//	private boolean passwordCheck(HttpServletRequest request) {
//		HttpSession session = request.getSession();
//		Boolean passwordCheck = (Boolean) session.getAttribute("CHECK_PASSWORD");
//		log.info("\t > CHECK_PASSWORD = {}", passwordCheck);
//		
//		return (passwordCheck == null) ? false : passwordCheck;
//	}

	private boolean passwordCheck() {
		Authentication authentication  = SecurityContextHolder.getContext().getAuthentication();
		log.info("\t > authentication = {}", authentication);
		if (authentication == null) {
			return false;
		}
		
		Object principal = authentication.getPrincipal();
		log.info("\t > principal = {}", principal);
		if (!(principal instanceof UserDetailsDto)) { // "anonymousUser"
			return false;
		}
		
		Member currentMember = ((UserDetailsDto) authentication.getPrincipal()).getMember();
		String passwordCheck = redisService.findValueByKey(currentMember.getMemberId());
		return "PASSWORD_CHECK".equals(passwordCheck);
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
		
		// \n --> \\n
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
	
	private void responseHTML(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		PrintWriter writer = response.getWriter();
		writer.println("<script>");
		writer.println("alert('" + message + "');");
		writer.println("location.href='" +  request.getContextPath() + "/member/account/checkPassword';");
		writer.println("</script>");
		writer.flush();
		writer.close();
	}
}
