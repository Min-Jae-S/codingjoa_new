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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.codingjoa.response.ErrorResponse;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CheckPasswordResetKeyInterceptor implements HandlerInterceptor {

	private final RedisService redisService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		log.info("## {}", this.getClass().getSimpleName());

		String key = request.getParameter("key");
		if (!checkPasswordResetKey(key)) {
			String message =  MessageUtils.getMessage("error.NotPasswordResetKey");
			//log.info("\t > original message = {}", message);
			
			message = StringUtils.removeEnd(message.replaceAll("\\.(\\s)*", ".\\\\n"), "\\n");
			//log.info("\t > processed message = {}", message);

			HandlerMethod handlerMethod = (HandlerMethod) handler;
			log.info("\t > {} '{}'", request.getMethod(), request.getRequestURI());
			if (handlerMethod.getBeanType().isAnnotationPresent(RestController.class)) {
				responseJSON(request, response, message);
			} else {
				responseHTML(request, response, message);
			}
			return false;
		}
		
		return true;
	}
	
	private boolean checkPasswordResetKey(String key) {
		log.info("\t > key = {}", key == null ? key : "'" + key + "'");
		return StringUtils.isEmpty(key) ? false : redisService.hasKey(key);
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
		
		PrintWriter writer = response.getWriter();
		writer.write(objectMapper.writeValueAsString(errorResponse)); // \n --> \\n
		writer.flush();
		writer.close();
	}
	
	private void responseHTML(HttpServletRequest request, HttpServletResponse response, String message)
			throws IOException {
		log.info("\t > respond HTML format");
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.TEXT_HTML.toString());
		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
		
		String script = "<script>";
		script += "alert('" + message + "');";
		script += "location.href='" + request.getContextPath() + "/member/findPassowrd';";
		script += "</script>";
		
		PrintWriter writer = response.getWriter();
		writer.write(script);
		writer.flush();
		writer.close();
	}
	
}