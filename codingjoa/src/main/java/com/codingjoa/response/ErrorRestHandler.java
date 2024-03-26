package com.codingjoa.response;

import javax.validation.ConstraintViolationException;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import com.codingjoa.exception.ExpectedException;
import com.codingjoa.response.ErrorResponse.ErrorResponseBuilder;
import com.codingjoa.test.TestException;
import com.codingjoa.test.TestResponse;
import com.codingjoa.test.TestResponse.TestResponseBuilder;

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
@RestControllerAdvice(annotations = RestController.class)
public class ErrorRestHandler {
	
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleException(Exception e) {
		log.info("@@ {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.Unknown")
				.build();
		log.info("\t > {}", errorResponse);
		
		//e.printStackTrace();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.InvalidFormat")
				.build();
		log.info("\t > {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());
		log.info("\t > field errors");
		e.getBindingResult().getFieldErrors().forEach(fieldError -> {
			log.info("\t\t - {} / {}", fieldError.getField(), fieldError.getCodes()[0]);
		}); 

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.bindingResult(e.getBindingResult())
				.build();
		log.info("\t > {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());
		log.info("\t > constraint violations");
		e.getConstraintViolations().forEach(violation -> {
			log.info("\t\t - invalid value = {}", violation.getInvalidValue());
		});

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.UNPROCESSABLE_ENTITY)
				.message(e.getMessage())
				.build();
		log.info("\t > {}", errorResponse);

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(errorResponse);
	}
	
	@ExceptionHandler(value =  {
		MissingPathVariableException.class,			// api/comments/
		MethodArgumentTypeMismatchException.class,	// api/comments/aa
	})
	protected ResponseEntity<Object> handleInvalidFormatException(Exception e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());

		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.InvalidFormat")
				.build();
		log.info("\t > {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > original message = {}", e.getMessage());
		
		ErrorResponse errorResponse = ErrorResponse.builder()
				.status(HttpStatus.BAD_REQUEST)
				.messageByCode("error.ExceedSize")
				.build();
		log.info("\t > {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	@ExceptionHandler(ExpectedException.class)
	protected ResponseEntity<Object> handleExpectedException(ExpectedException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > error code = {} / error field = {}", e.getCode(), e.getField());
		
		ErrorResponseBuilder builder = ErrorResponse.builder().status(HttpStatus.BAD_REQUEST);
		if (e.getField() == null) { 
			builder.messageByCode(e.getCode());
		} else { 
			ErrorDetails errorDetails = ErrorDetails.builder()
					.field(e.getField())
					.messageByCode(e.getCode())
					.build();
			builder.details(errorDetails);
		}
		
		ErrorResponse errorResponse = builder.build();
		log.info("\t > {}", errorResponse);
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
	}
	
	// TEST
	@ExceptionHandler(TestException.class)
	protected ResponseEntity<Object> handleTestException(TestException e) {
		log.info("## {} - {}", this.getClass().getSimpleName(), e.getClass().getSimpleName());
		log.info("\t > location = {}", e.getStackTrace()[0]);
		log.info("\t > error code = {} / error field = {}", e.getCode(), e.getField());
		
		TestResponseBuilder builder = TestResponse.builder().status(HttpStatus.BAD_REQUEST);
		if (e.getField() == null) { 
			builder.messageByCode(e.getCode());
		} else { 
			ErrorDetails errorDetails = ErrorDetails.builder()
					.field(e.getField())
					.messageByCode(e.getCode())
					.build();
			builder.details(errorDetails);
		}
		
		TestResponse testResponse = builder.build();
		log.info("\t > {}", testResponse);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(testResponse);
	}
	
}
