package com.codingjoa.error;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice 
public class ErrorHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("============== handleMethodArgumentNotValidException ==============");
		
		ErrorResponse respone = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("{}", respone);
		
		return ResponseEntity.unprocessableEntity().body(respone);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("============== handleMaxUploadSizeExceededException ==============");
		
		ErrorResponse respone = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("{}", respone);
		
		return ResponseEntity.badRequest().body(respone);
	}
}
