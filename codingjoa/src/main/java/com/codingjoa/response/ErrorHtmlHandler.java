package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

// @ExceptionHandler는 @Controller, @RestController가 적용된 Bean에서 
// 발생한 예외를 잡아 하나의 메소드에서 처리하는 역할을 한다. @Service에서의 예외는 잡지 못한다.
@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
//	@ExceptionHandler(Exception.class)
//	protected String handleException(Exception e) {
//		log.info("[ErrorHtmlHandler] {}", e.getClass().getName());
//		log.info("message = {}", e.getMessage());
//		
//		return "forward:/error/errorPage";
//	}

	@ExceptionHandler(RuntimeException.class)
	protected String handleRuntimeException(RuntimeException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getName());
		log.info("message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("{}", response);
//		
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//	
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected String handleConstraintViolationException(ConstraintViolationException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());

//		e.getConstraintViolations().forEach(v -> {
//			log.info("Invalid Value = {}", v.getInvalidValue());
//			log.info("{}", v);
//		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(BindException.class)
	protected String handleBindException(BindException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
//		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
//			log.info("field = {}", fieldError.getField());
//			log.info("code = {}", fieldError.getCodes()[0]);
//		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected String handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}

//	@ExceptionHandler(MissingServletRequestParameterException.class)
//	protected String handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
//		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
//		log.info("message = {}", e.getMessage());
//		
//		return "forward:/error/errorPage";
//	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	protected String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
}
