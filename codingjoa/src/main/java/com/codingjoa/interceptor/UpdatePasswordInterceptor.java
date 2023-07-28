package com.codingjoa.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdatePasswordInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {} : preHandle", this.getClass().getSimpleName());
		
		if (!passwordCheck(request)) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				responseJSON(request, response);
			} else {
				responseHTML(request, response);
			}
			return false;
		}
		
		return true;
	}
	
	private boolean passwordCheck(HttpServletRequest request) {
		HttpSession session = request.getSession();
		Boolean passwordCheck = (Boolean) session.getAttribute("CHECK_PASSWORD");
		log.info("\t > CHECK_PASSWORD = {}", passwordCheck);
		
		if (passwordCheck == null) {
			return false;
		}
		
		if (!passwordCheck) {
			return false;
		}
		
		return true;
	}
	
	private void responseJSON(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException, IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		String message =  MessageUtils.getMessage("error.NotCheckPassword");
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".<br>"), "<br>");
		log.info("\t > processed message = {}", message);
		
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
		response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
	}
	
	private void responseHTML(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		String message =  MessageUtils.getMessage("error.NotCheckPassword");
		message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
		log.info("\t > processed message = {}", message);
		
		PrintWriter writer = response.getWriter();
		writer.println("<script>");
		writer.println("alert('" + message + "');");
		writer.println("location.href='" +  request.getContextPath() + "/member/account/checkPassword';");
		writer.println("</script>");
		writer.flush();
	}
}
