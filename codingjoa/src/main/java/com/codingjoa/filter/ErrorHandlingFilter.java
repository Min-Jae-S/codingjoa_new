package com.codingjoa.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingjoa.converter.NullToEmptyStringSerializer;
import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.AjaxUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ErrorHandlingFilter extends OncePerRequestFilter {
	
	private static final String FORWARD_URL = "/error";
	private final ObjectMapper objectMapper;
	
	public ErrorHandlingFilter() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:ss:mm");
		this.objectMapper =  Jackson2ObjectMapperBuilder
				.json()
				.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer(formatter))
				.featuresToEnable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
				.build();
		objectMapper.getSerializerProvider().setNullValueSerializer(new NullToEmptyStringSerializer());
	}
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (Exception e) {
			log.info("## {}.doFilterInternal", this.getClass().getSimpleName());
			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
			
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			
			ErrorResponse errorResponse = ErrorResponse.builder()
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.messageByCode("error.server")
					.build();
			
			if (AjaxUtils.isAjaxRequest(request)) {
				log.info("\t > respond with errorResponse in JSON format");
				String jsonResponse = objectMapper.writeValueAsString(errorResponse);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(jsonResponse);
				response.getWriter().close();
			} else {
				log.info("\t > forward to '{}'", FORWARD_URL);
				request.setAttribute("errorResponse", errorResponse);
				request.getRequestDispatcher(FORWARD_URL).forward(request, response);
			}
		}
	}
}
