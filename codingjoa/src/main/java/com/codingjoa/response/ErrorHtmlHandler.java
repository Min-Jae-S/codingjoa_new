package com.codingjoa.response;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
	@ExceptionHandler(Exception.class) // NoHandlerFoundException
	protected String handleException(Exception e, HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());

//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("\t > {}", response);
//		
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//	
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(BindException.class) // /board/write?boardCategory=aa, /board/modify?boardIdx=aa, /board/deleteProc?boardIdx=aa
	protected String handleBindException(BindException e, HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t > field = {}", fieldError.getField());
			log.info("\t > code = {}", fieldError.getCodes()[0]);
		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(ConstraintViolationException.class) // /board/main?boardCategoryCode=11
	protected String handleConstraintViolationException(ConstraintViolationException e, 
			HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());
		
		e.getConstraintViolations().forEach(violation -> {
			//log.info("\t > {}", violation);
			log.info("\t > invalid value = {}", violation.getInvalidValue());
		});
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(value = { 
		MissingServletRequestParameterException.class,	// /board/read
		MethodArgumentTypeMismatchException.class		// /board/read?boardIdx=, /board/read?boardIdx=aa 
	})
	protected String handlePathVariableExceptionAndTypeMismatchException(Exception e, HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());
		
		return "forward:/error/errorPage";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected String handleIllegalArgumentException(IllegalArgumentException e, HttpServletRequest request) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		request.setAttribute("errorMessage", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
}
