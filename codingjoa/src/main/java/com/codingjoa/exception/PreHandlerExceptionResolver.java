package com.codingjoa.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PreHandlerExceptionResolver implements HandlerExceptionResolver {
	
	//private static final String FORWARD_URL = "/error"; 
	//private final ObjectMapper objectMapper;
	private final HandlerExceptionResolver exceptionMvcHandler;
	private final HandlerExceptionResolver exceptionRestHandler;
	
	public PreHandlerExceptionResolver(@Qualifier("exceptionMvcHandler") HandlerExceptionResolver exceptionMvcHandler,
			@Qualifier("exceptionRestHandler") HandlerExceptionResolver exceptionRestHandler) {
		this.exceptionMvcHandler = exceptionMvcHandler;
		this.exceptionRestHandler = exceptionRestHandler;
	}
	
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
		
		log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
		
		if (AjaxUtils.isAjaxRequest(request)) {
			log.info("\t > ajax request detected, handling via ExceptionRestHandler");
			return exceptionRestHandler.resolveException(request, response, handler, ex);
		} else {
			log.info("\t > non-ajax request detected, handling via ExceptionMvcHandler");
			return exceptionMvcHandler.resolveException(request, response, handler, ex);
		}
	
	}



	

//	@Override
//	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, 
//			Exception ex) {
//		log.info("## {}", this.getClass().getSimpleName());
//		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
//		log.info("\t > handler = {}", handler != null ? handler.getClass().getSimpleName() : null);
//		
//		if (handler != null) {
//			log.info("\t > delegate exception handling to the ExceptionHandlerExceptionResolver");
//			return null;
//		}
//		
//		try {
//			log.info("\t > {}: {}", ex.getClass().getSimpleName(), ex.getMessage());
//			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
//			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//			ErrorResponseBuilder builder = ErrorResponse.builder().status(HttpStatus.NOT_FOUND);
//			
//			if (AjaxUtils.isAjaxRequest(request)) {
//				ErrorResponse errorResponse = builder.messageByCode("error.NotFoundResource").build();
//
//				log.info("\t > respond with errorResponse in JSON format");
//				String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//				response.getWriter().write(jsonResponse);
//				response.getWriter().close();
//				return new ModelAndView();
//			} else {
//				ErrorResponse errorResponse = builder.messageByCode("error.NotFoundPage").build();
//				
//				log.info("\t > forward to '{}'", FORWARD_URL);
//				request.setAttribute("errorResponse", errorResponse);
//				return new ModelAndView("forward:" + FORWARD_URL);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		return null;
//	}
	
}
