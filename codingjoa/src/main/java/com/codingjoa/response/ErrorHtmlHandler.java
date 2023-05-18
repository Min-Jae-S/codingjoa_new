package com.codingjoa.response;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
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
	
	@ExceptionHandler(Exception.class)
	protected String handleException(Exception e, HttpServletResponse response, Model model) throws Exception {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());

		response.setStatus(499);
		model.addAttribute("errorMessage", e.getMessage());
		
		return "error/error-page";
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected String handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());

		//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("{}", response);
//		
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//	
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
		
		return "error/error-page";
	}
	
	@ExceptionHandler(BindException.class) // /board/write?boardCategory=aa, /board/modify?boardIdx=aa, /board/deleteProc?boardIdx=aa
	protected String handleBindException(BindException e) {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("field = {}", fieldError.getField());
			log.info("code = {}", fieldError.getCodes()[0]);
		});
		
		return "error/error-page";
	}
	
	@ExceptionHandler(ConstraintViolationException.class) // /board/main?boardCategoryCode=11
	protected String handleConstraintViolationException(ConstraintViolationException e) {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());

		e.getConstraintViolations().forEach(v -> {
			log.info("{}", v);
			log.info("invalid value = {}", v.getInvalidValue());
		});
		
		return "error/error-page";
	}
	
	@ExceptionHandler(value = { 
		MissingServletRequestParameterException.class,	// /board/read
		MethodArgumentTypeMismatchException.class		// /board/read?boardIdx=, /board/read?boardIdx=aa 
	})
	protected String handleException(Exception e) {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());
		
		return "error/error-page";
	}

	@ExceptionHandler(IllegalArgumentException.class)
	protected String handleIllegalArgumentException(IllegalArgumentException e) {
		log.info("-------- {}: {} --------", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t message = {}", e.getMessage());
		
		return "error/error-page";
	}
	
}
