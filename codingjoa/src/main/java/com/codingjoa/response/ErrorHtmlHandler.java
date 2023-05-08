package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

/*
 * 400 Bad Request
 * 401 Unauthorized
 * 403 Forbidden
 * 404 Not Found
 */

@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
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
	
	@ExceptionHandler(BindException.class)
	protected String handleBindException(BindException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("field = {}", fieldError.getField());
			log.info("code = {}", fieldError.getCodes()[0]);
		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(ConstraintViolationException.class) // /board/main?boardCategoryCode=11
	protected String handleConstraintViolationException(ConstraintViolationException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());

		e.getConstraintViolations().forEach(v -> {
			log.info("{}", v);
			log.info("invalid value = {}", v.getInvalidValue());
		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(value = { 
		MissingServletRequestParameterException.class,	// /board/read
		MethodArgumentTypeMismatchException.class		// /board/read?boardIdx=, /board/read?boardIdx=aa 
	})
	protected String handleException(Exception e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("[ErrorHtmlHandler] {}", e.getClass().getSimpleName());
		log.info("message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
}
