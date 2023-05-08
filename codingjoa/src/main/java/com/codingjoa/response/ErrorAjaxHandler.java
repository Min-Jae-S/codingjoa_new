package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class ErrorAjaxHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("[ErrorAjaxHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("errorResponse = {}", errorResponse);
		
		return ResponseEntity.unprocessableEntity().body(errorResponse);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
		log.info("[ErrorAjaxHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.create().errorMessage(e.getMessage());
		log.info("errorResponse = {}", errorResponse);

		return ResponseEntity.badRequest().body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.info("[ErrorAjaxHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.create().errorMessage(e.getMessage());
		log.info("errorResponse = {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("[ErrorAjaxHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.create().errorMessage(e.getMessage());
		log.info("errorResponse = {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("[ErrorAjaxHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.create().errorCode("error.ExceedSize");
		log.info("errorResponse = {}", errorResponse);
		
		return ResponseEntity.badRequest().body(errorResponse);
	}
	
}
