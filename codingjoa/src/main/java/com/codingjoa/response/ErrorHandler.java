package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import lombok.extern.slf4j.Slf4j;

// @ExceptionHandler는 @Controller, @RestController가 적용된 Bean에서 발생한 예외를 잡아 
// 하나의 메소드에서 처리하는 역할을 한다. @Service에서의 예외는 잡지 못한다.
@ControllerAdvice 
@Slf4j
public class ErrorHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
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
	public String handleConstraintViolationException(ConstraintViolationException e) {
		log.info("## ConstraintViolationException");
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(BindException.class)
	public String handleBindException(BindException e) {
		log.info("## BindException");
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("field = {}", fieldError.getField());
			log.info("code = {}", fieldError.getCodes()[0]);
		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## IllegalArgumentException");
		log.info("## error message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	@ResponseBody
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("## MaxUploadSizeExceededException");
		
		ErrorResponse response = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("response = {}", response);
		
		return ResponseEntity.badRequest().body(response);
	}
	
	
}
