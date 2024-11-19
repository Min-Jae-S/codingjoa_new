package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {

	@RequestMapping
	public String error(HttpServletRequest request, Model model) {
		log.info("## error");
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));

		ErrorResponse errorResponse = null;
		Object obj = request.getAttribute("errorResponse");
		
		if (obj instanceof ErrorResponse) {
			log.info("\t > errorResponse from exceptionHandler");
			errorResponse = (ErrorResponse) obj;
		} else {
			log.info("\t > no errorResponse from exceptionHandler, create default errorResponse");
			errorResponse = ErrorResponse.builder()
					.status(HttpStatus.BAD_REQUEST)
					.messageByCode("error.Global")
					.build();
		}
		
		model.addAttribute("errorResponse", errorResponse);
		log.info("\t > errorResponse = {}", errorResponse);
		
		return "error/error";
	}
}
