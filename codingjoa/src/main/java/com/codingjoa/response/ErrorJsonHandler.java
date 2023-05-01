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
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.codingjoa.security.exception.NotFoundEntityException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = RestController.class)
public class ErrorJsonHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## ErrorJsonHandler.MethodArgumentNotValidException");
		
		ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("response = {}", response);
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
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
	protected ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## ErrorJsonHandler.IllegalArgumentException");
		
		ErrorResponse response = ErrorResponse.create().errorMessage(e.getMessage());
		log.info("response = {}", response);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
	
	@ExceptionHandler(NotFoundEntityException.class)
	protected ResponseEntity<Object> handleNotFoundEntityException(NotFoundEntityException e) {
		log.info("## ErrorJsonHandler.NotFoundEntityException");
		
		ErrorResponse response = ErrorResponse.create().errorCode(e.getErrorCode());
		log.info("response = {}", response);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("## ErrorJsonHandler.MaxUploadSizeExceededException");
		
		ErrorResponse response = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("response = {}", response);
		
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
	}
}
