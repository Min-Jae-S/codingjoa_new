package com.codingjoa.exception;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.dto.ErrorResponse.ErrorResponseBuilder;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@RequiredArgsConstructor
@Component
public class PreHandlerExceptionResolver implements HandlerExceptionResolver {
	
	private static final String FORWARD_URL = "/error"; 
	private final ObjectMapper objectMapper;
	//private final RequestMappingHandlerMapping handlerMapping;

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		log.info("\t > handler = {}", handler != null ? handler.getClass().getSimpleName() : null);
		
		if (handler != null) {
			log.info("\t > delegate exception handling to the ExceptionHandlerExceptionResolver");
			return null;
		}
		
		try {
			log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			ErrorResponseBuilder builder = ErrorResponse.builder().status(HttpStatus.NOT_FOUND);
			
			if (AjaxUtils.isAjaxRequest(request)) {
				ErrorResponse errorResponse = builder.messageByCode("error.NotFoundResource").build();

				log.info("\t > respond with errorResponse in JSON format");
				String jsonResponse = objectMapper.writeValueAsString(errorResponse);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.getWriter().write(jsonResponse);
				response.getWriter().close();
				return new ModelAndView();
			} else {
				ErrorResponse errorResponse = builder.messageByCode("error.NotFoundPage").build();
				
				log.info("\t > forward to '{}'", FORWARD_URL);
				request.setAttribute("errorResponse", errorResponse);
				return new ModelAndView("forward:" + FORWARD_URL);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
}
