package com.codingjoa.error;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.codingjoa.dto.ErrorDetails;
import com.codingjoa.dto.ErrorResponse;
import com.codingjoa.dto.ErrorResponse.ErrorResponseBuilder;
import com.codingjoa.test.TestException;
import com.codingjoa.test.TestResponse;

import lombok.extern.slf4j.Slf4j;

/*
 * 
 * 400 Bad Request	
 *   The server cannot or will not process the request 
 *   due to something that is perceived to be a client error 
 *   (e.g., malformed request syntax, invalid request message framing)
 *   
 * 401 Unauthorized
 *   semantically this response means "unauthenticated"
 *   
 * 403 Forbidden
 *   The client does not have access rights to the content; 
 *   that is, it is unauthorized, so the server is refusing to give the requested resource.
 *   
 * 404 Not Found
 *   The server cannot find the requested resource. 
 *   In the browser, this means the URL is not recognized. 
 *   In an API, this can also mean that the endpoint is valid but the resource itself does not exist.
 * 
 */

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice(annotations = ResponseBody.class)
public class ExceptionRestHandler {
	
	//@ResponseStatus(HttpStatus.BAD_REQUEST) // HTTP status codes are explicitly set via ResponseEntity, so there's no need to use @ResponseStatus.
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e, e.getMessage());
		//e.printStackTrace();

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.global")
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(NoHandlerFoundException.class) 
	protected ResponseEntity<Object> handleNoHandlerFoundEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleNoHandlerFoundEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.NOT_FOUND)
				.messageByCode("error.notFoundResource") 
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
	}
	
	@ExceptionHandler(BindException.class)
	protected ResponseEntity<Object> handleBindEx(BindException e, HttpServletRequest request) {
		log.info("## {}.handleBindEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t > errorField = {}, errorCode = {}", fieldError.getField(), fieldError.getCodes()[0]);
		}); 
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.bindingResult(e.getBindingResult())
				.build();

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> handleHttpMessageNotReadableEx(HttpMessageNotReadableException e, HttpServletRequest request) {
		log.info("## {}.handleHttpMessageNotReadableEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.notValidFormat")
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidEx(MethodArgumentNotValidException e, HttpServletRequest request) {
		log.info("## {}.handleMethodArgumentNotValidEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t > errorField = {}, errorCode = {}", fieldError.getField(), fieldError.getCodes()[0]);
		}); 

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.bindingResult(e.getBindingResult())
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationEx(ConstraintViolationException e, HttpServletRequest request) {
		log.info("## {}.handleConstraintViolationEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		e.getConstraintViolations().forEach(violation -> log.info("> invalid value = {}", violation.getInvalidValue()));

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.message(e.getMessage())
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(value =  {
		MissingPathVariableException.class,			// api/comments/
		MethodArgumentTypeMismatchException.class,	// api/comments/aa
	})
	protected ResponseEntity<Object> handleInvalidFormatEx(Exception e, HttpServletRequest request) {
		log.info("## {}.handleInvalidFormatEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.notValidFormat")
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxUploadSizeExceededEx(MaxUploadSizeExceededException e, HttpServletRequest request) {
		log.info("## {}.handleMaxUploadSizeExceededEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.exceededSize")
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(ExpectedException.class)
	protected ResponseEntity<Object> handleExpectedEx(ExpectedException e, HttpServletRequest request) {
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
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(builder.build());
	}
	
	@ExceptionHandler(TestException.class)
	protected ResponseEntity<Object> handleTestEx(TestException e, HttpServletRequest request) {
		log.info("## {}.handleTestEx", this.getClass().getSimpleName());
		log.info("\t > {}: {}", e.getClass().getSimpleName(), e.getMessage());
		
		TestResponse errorResponse = TestResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.message(e.getErrorMessage())
				.build();
		
		log.info("\t > respond with errorResponse in JSON format");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
}
