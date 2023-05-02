package com.codingjoa.response;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorGlobalHandler {

	@ExceptionHandler(Exception.class)
	protected void handleException(Exception e) {
		log.info("## ErrorGlobalHandler, {}", e.getClass().getName());
		log.info("message = {}", e.getMessage());
	}
}
