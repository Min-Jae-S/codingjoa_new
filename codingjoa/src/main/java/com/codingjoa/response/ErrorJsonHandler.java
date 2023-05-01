package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class ErrorJsonHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## MethodArgumentNotValidException");
		
		ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("response = {}", response);
			
		return ResponseEntity.unprocessableEntity().body(response);
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
//			HttpServletRequest request) throws ModelAndViewDefiningException {
//		log.info("## handle MethodArgumentNotValidException");
//		
//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("{}", response);
//			
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//		
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
//	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
		log.info("## ConstraintViolationException");

		return null;
	}
	
	@ExceptionHandler(BindException.class)
	public ResponseEntity<Object> handleBindException(BindException e) {
		log.info("## BindException");
		
		ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
		log.info("response = {}", response);
		
		return ResponseEntity.unprocessableEntity().body(response);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## IllegalArgumentException");
		
		return null;
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("## MaxUploadSizeExceededException");
		
		ErrorResponse response = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("response = {}", response);
		
		return ResponseEntity.badRequest().body(response);
	}
}
