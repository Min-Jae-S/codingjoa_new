package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/error")
@Controller
public class ErrorController {

	@GetMapping
	public String error(HttpServletRequest request) {
		log.info("## error");
		log.info("\t > request-line = {}", HttpUtils.getHttpRequestLine(request));
		
		ErrorResponse errorResponse = (ErrorResponse) request.getAttribute("errorResponse");
		if (errorResponse == null) {
			return "redirect:/";
		}
		
		log.info("\t > status = {}", errorResponse.getStatus());
		log.info("\t > message = {}", errorResponse.getMessage());
		
		return "error/error";
	}
}
