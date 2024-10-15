package com.codingjoa.exception;

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
import com.codingjoa.test.TestResponse;
import com.codingjoa.test.TestResponse.TestResponseBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class ErrorHtmlHandler {
	
	private static final String FORWARD_URL = "/error";
	
	@ExceptionHandler(Exception.class) // NoHandlerFoundException, NestedServletException etc..
	protected String handleEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		//e.printStackTrace();
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.Global") // error.Unknown --> error.Global
				.build();
		request.setAttribute("errorResponse", errorResponse);
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(NoHandlerFoundException.class) 
	protected String handleNoHandlerFoundEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleNoHandlerFoundEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.messageByCode("error.NotFoundPage")
				.build();
		request.setAttribute("errorResponse", errorResponse);
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(BindException.class)
	protected String handleBindEx(BindException e, HttpServletRequest request) {
		log.info("## {}.handleBindEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t > errorField = {}, errorCode = {}", fieldError.getField(), fieldError.getCodes()[0]);
		}); 
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.bindingResult(e.getBindingResult())
				.build();
		request.setAttribute("errorResponse", errorResponse);

//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("\t > {}", response);
//
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(ConstraintViolationException.class) // /board/?boardCategoryCode=11
	protected String handleConstraintViolationEx(ConstraintViolationException e, 
			HttpServletRequest request) {
		log.info("## {}.handleConstraintViolationEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
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
	protected String handleInvalidFormatEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleInvalidFormatEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.InvalidFormat")
				.build();
		request.setAttribute("errorResponse", errorResponse);
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}

	@ExceptionHandler(ExpectedException.class)
	protected String handleExpectedEx(ExpectedException e, HttpServletRequest request) {
		log.info("## {}.handleExpectedEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
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
		request.setAttribute("errorResponse", builder.build());
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}
	
	@ExceptionHandler(TestException.class)
	protected String handleTestEx(TestException e, HttpServletRequest request) {
		log.info("## {}.handleTestEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
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
		request.setAttribute("errorResponse", builder.build());
		
		log.info("\t > forward to '{}'", FORWARD_URL);
		return "forward:" + FORWARD_URL;
	}
	
}
