package com.codingjoa.handler;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.codingjoa.dto.ErrorDetails;
import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.dto.ErrorResponse.ErrorResponseBuilder;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.test.TestException;
import com.codingjoa.test.TestResponse;
import com.codingjoa.test.TestResponse.TestResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
	public static final String FORWARD_URL = "/error/errorPage";
	
	@ExceptionHandler(Exception.class) // NoHandlerFoundException, NestedServletException etc..
	protected String handleException(Exception e, HttpServletRequest request) {
		log.info("## {} (Global) - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.Global") // error.Unknown --> error.Global
				.build();
		
		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	
		
		//e.printStackTrace();
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(NoHandlerFoundException.class) 
	protected String handleNoHandlerFoundException(Exception e, HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.messageByCode("error.NotFoundPage")
				.build();
		
		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	
		
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(BindException.class)
	protected String handleBindException(BindException e, HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > message = {}", e.getMessage());
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t > errorField = {}, errorCode = {}", fieldError.getField(), fieldError.getCodes()[0]);
		}); 
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.bindingResult(e.getBindingResult())
				.build();

		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	
		
//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("\t > {}", response);
//
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
		
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(ConstraintViolationException.class) // /board/?boardCategoryCode=11
	protected String handleConstraintViolationException(ConstraintViolationException e, 
			HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > message = {}", e.getMessage());
		e.getConstraintViolations().forEach(violation -> {
			log.info("\t > invalid value = {}", violation.getInvalidValue());
		});
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.message(e.getMessage())
				.build();

		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	
		
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(value = { 
		MissingServletRequestParameterException.class,	// /board/read
		MethodArgumentTypeMismatchException.class		// /board/read?boardIdx=, /board/read?boardIdx=aa 
	})
	protected String handleInvalidFormatException(Exception e, HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.InvalidFormat")
				.build();
		
		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	
		
		return "forward:" + FORWARD_URL;
	}

	@ExceptionHandler(ExpectedException.class)
	protected String handleExpectedException(ExpectedException e, HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > errorCode = {}, errorField = {}", e.getErrorCode(), e.getErrorField());
		
		ErrorResponseBuilder builder = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST);
		if (e.getErrorField() == null) { 
			builder.messageByCode(e.getErrorCode());
		} else { 
			ErrorDetails errorDetails = ErrorDetails.builder()
					.field(e.getErrorField())
					.messageByCode(e.getErrorCode())
					.build();
			builder.details(errorDetails);
		}
		
		ErrorResponse errorResponse = builder.build();
		request.setAttribute("errorResponse", errorResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);	

		return "forward:" + FORWARD_URL;
	}
	
	// TEST
	@ExceptionHandler(TestException.class)
	protected String handleTestException(TestException e, HttpServletRequest request) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > errorCode = {}, errorField = {}", e.getErrorCode(), e.getErrorField());
		
		TestResponseBuilder builder = TestResponse.builder().status(HttpStatus.BAD_REQUEST);
		if (e.getErrorField() == null) { 
			builder.messageByCode(e.getErrorCode());
		} else { 
			ErrorDetails errorDetails = ErrorDetails.builder()
					.field(e.getErrorField())
					.messageByCode(e.getErrorCode())
					.build();
			builder.details(errorDetails);
		}
		
		TestResponse testResponse = builder.build();
		request.setAttribute("errorResponse", testResponse);
		log.info("\t > forward to '{}'", FORWARD_URL);		
		
		return "forward:" + FORWARD_URL;
	}
	
}
