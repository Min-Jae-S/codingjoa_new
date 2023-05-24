package com.codingjoa.response;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
	@ExceptionHandler(Throwable.class) // NoHandlerFoundException
	protected String handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) throws IOException {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());

		//response.setStatus(499);
		request.setAttribute("errorMessage", e.getMessage());
		response.sendError(400);
		
		return "forward:/error/errorPage";
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());

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
	protected String handleBindException(BindException e) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
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
	protected String handlePathVariableExceptionAndTypeMismatchException(Exception e) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("## {} : {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > message = {}", e.getMessage());
		
		return "forward:/error/errorPage";
	}
	
}
