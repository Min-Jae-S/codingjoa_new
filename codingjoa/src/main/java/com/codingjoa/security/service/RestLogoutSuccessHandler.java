package com.codingjoa.security.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
@Component
public class RestLogoutSuccessHandler implements LogoutSuccessHandler {
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > authentication token = {}", authentication);
		
//		SuccessResponse successResponse = SuccessResponse.builder()
//				.status(HttpStatus.OK)
//				.messageByCode("success.Logout")
//				.data(Map.of("redirectUrl", request.getContextPath() + "/login"))
//				.build();
//		log.info("\t > {}", successResponse);
//		
//		response.setStatus(HttpStatus.OK.value());
//		response.setContentType(MediaType.APPLICATION_JSON_VALUE.toString());
//		response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
//		response.getWriter().write(convertObjectToJson(successResponse));
//		response.getWriter().close();
		
		String redirectUrl = request.getParameter("redirect");

		if (!StringUtils.hasText(redirectUrl)) {
			redirectUrl = request.getContextPath() + "/";
		}
		
		log.info("\t > redirect to '{}'", redirectUrl);
		response.sendRedirect(redirectUrl);

		//redirectStrategy.sendRedirect(request, response, redirectUrl);
	}
	
	private String convertObjectToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = Jackson2ObjectMapperBuilder
				.json()
				.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ISO_LOCAL_DATE_TIME)) // yyyy-MM-dd'T'HH:ss:mm"
				.build();
		return objectMapper.writeValueAsString(object);
	}

}
