package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.RequestUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {

	/*
	 * @@ Considering alternatives to block direct access to URL: /error
	 * 	1. using dispatcherType to differentiate between request and forward: 
	 *   	- check if the request is a direct access (dispatcherType == DispatcherType.REQUEST)
	 * 	2. using Spring Security configuration:
	 *   	- deny direct access to specific URLs using the 'denyAll()' method or custom access control
	 */
	
	@RequestMapping
	public String error(HttpServletRequest request, HttpServletResponse response, Model model) {
		log.info("## error");
		log.info("\t > request-line = {} [{}]", RequestUtils.getRequestLine(request), request.getDispatcherType());

		ErrorResponse errorResponse = null;
		Object obj = request.getAttribute("errorResponse");
		
		if (obj instanceof ErrorResponse) {
			errorResponse = (ErrorResponse) obj;
		} else {
			log.info("\t > missing 'errorResponse' in request, using default and setting response status 400 (BAD_REQUEST)");
			errorResponse = ErrorResponse.builder()
					.status(HttpStatus.BAD_REQUEST)
					.messageByCode("error.global")
					.build();
			response.setStatus(HttpStatus.BAD_REQUEST.value());
		}
		
		model.addAttribute("errorResponse", errorResponse);
		log.info("\t > errorResponse = {}", errorResponse);
		
		return "error/error";
	}
}
