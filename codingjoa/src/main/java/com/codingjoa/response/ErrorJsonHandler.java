package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class ErrorJsonHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## ErrorJsonHandler.MethodArgumentNotValidException");
		
		ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("response = {}", response);
			
		return ResponseEntity.unprocessableEntity().body(response);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
		log.info("## ErrorJsonHandler.ConstraintViolationException");
		log.info("message = {}", e.getMessage());

		return null;
	}
	
//	@ExceptionHandler(BindException.class)
//	public ResponseEntity<Object> handleBindException(BindException e) {
//		log.info("## ErrorJsonHandler.BindException");
//		log.info("message = {}", e.getMessage());
//		
//		ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//		log.info("response = {}", response);
//		
//		return null;
//	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## ErrorJsonHandler.IllegalArgumentException");
		log.info("message = {}", e.getMessage());
		
		return null;
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("## ErrorJsonHandler.MaxUploadSizeExceededException");
		
		ErrorResponse response = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("response = {}", response);
		
		return ResponseEntity.badRequest().body(response);
	}
}
