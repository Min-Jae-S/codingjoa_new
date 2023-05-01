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

//@ExceptionHandler는 @Controller, @RestController가 적용된 Bean에서 
//발생한 예외를 잡아 하나의 메소드에서 처리하는 역할을 한다. 
//@Service에서의 예외는 잡지 못한다.
@Slf4j
@ControllerAdvice 
public class ErrorHtmlHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## MethodArgumentNotValidException, message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	public String handleConstraintViolationException(ConstraintViolationException e) {
		log.info("## ConstraintViolationException, message = {}", e.getMessage());

//		e.getConstraintViolations().forEach(v -> {
//			log.info("Invalid Value = {}", v.getInvalidValue());
//			log.info("{}", v);
//		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(BindException.class)
	public String handleBindException(BindException e) {
		log.info("## BindException", e.getMessage());
		
//		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
//			log.info("field = {}", fieldError.getField());
//			log.info("code = {}", fieldError.getCodes()[0]);
//		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## IllegalArgumentException: {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}

}
