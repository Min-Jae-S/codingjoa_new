package com.codingjoa.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.AjaxUtils;
import com.codingjoa.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
public class ErrorHandlingFilter implements Filter {
	
	private static final String FORWARD_URL = "/error";
	private ObjectMapper objectMapper;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		log.info("## {}.init", filterConfig.getFilterName());
		WebApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
        if (context != null) {
        	objectMapper = context.getBean(ObjectMapper.class);
        } else {
        	objectMapper = new ObjectMapper();
        }
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		log.info("## {}.doFilter", this.getClass().getSimpleName());
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		log.info("\t > dispatcherType = {}", request.getDispatcherType());
		
		Throwable throwable = (Throwable) request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
		log.info("\t > error msg = {}", throwable.getMessage());
		
//		try {
//			chain.doFilter(servletRequest, servletResponse);
//		} catch (Exception e) {
//			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
//			log.info("## {}.doFilter", this.getClass().getSimpleName());
//			log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
//			log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
//			
//			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//			
//			ErrorResponse errorResponse = ErrorResponse.builder()
//					.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.messageByCode("error.Server")
//					.build();
//			
//			if (AjaxUtils.isAjaxRequest(request)) {
//				log.info("\t > respond with errorResponse in JSON format");
//				String jsonResponse = objectMapper.writeValueAsString(errorResponse);
//				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//				response.getWriter().write(jsonResponse);
//				response.getWriter().close();
//			} else {
//				log.info("\t > forward to '{}'", FORWARD_URL);
//				request.setAttribute("errorResponse", errorResponse);
//				request.getRequestDispatcher(FORWARD_URL).forward(request, response);
//			}
//		}
	}
	
}
