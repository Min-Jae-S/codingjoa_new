package com.codingjoa.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice 
public class ErrorHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("============== ErrorHandler ==============");
		//log.info("MethodArgumentNotValidException", e);
		
		ErrorResponse respone = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("{}", respone);
		
		return ResponseEntity.unprocessableEntity().body(respone);
	}
}
