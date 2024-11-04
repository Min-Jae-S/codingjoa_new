package com.codingjoa.exception;

import java.nio.charset.StandardCharsets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.dto.ErrorResponse.ErrorResponseBuilder;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Component
public class PreHandlerExceptionResolver implements HandlerExceptionResolver {

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
			Exception ex) {
		log.info("## {}", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		
		if (handler != null) {
			return null;
		}
		
		response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		ErrorResponseBuilder builder = ErrorResponse.builder().status(HttpStatus.NOT_FOUND);
		
		if (AjaxUtils.isAjaxRequest(request)) {
			ErrorResponse errorResponse = builder.messageByCode("error.NotFoundResource").build();
			// return json
		} else {
			ErrorResponse errorResponse = builder.messageByCode("error.NotFoundPage").build();
			// return string(forward)
		}

		return null;
	}
	
}
