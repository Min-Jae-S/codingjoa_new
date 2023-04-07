package com.codingjoa.response;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import lombok.extern.slf4j.Slf4j;

// @ExceptionHandler는 @Controller, @RestController가 적용된 Bean에서 발생한 예외를 잡아 
// 하나의 메소드에서 처리하는 역할을 한다. @Service에서의 예외는 잡지 못한다.
@RestControllerAdvice 
@Slf4j
public class ErrorHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public String handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
			HttpServletRequest request) throws ModelAndViewDefiningException {
		log.info("============== MethodArgumentNotValidException ==============");
		
		if (isAjaxRequest(request)) {
			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
			log.info("{}", response);
			
			request.setAttribute("errorResponse", ResponseEntity.unprocessableEntity().body(response));
			
			return "forward:/error/422";
		}
		
		log.info("e.getParameter={}", e.getParameter());
		
		return "forward:/error/errorPage";
	}
	
//	@ExceptionHandler(MethodArgumentNotValidException.class)
//	public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException e,
//			HttpServletRequest request) throws ModelAndViewDefiningException {
//		log.info("============== MethodArgumentNotValidException ==============");
//		
//		if (isAjaxRequest(request)) {
//			ErrorResponse response = ErrorResponse.create().bindingResult(e.getBindingResult());
//			log.info("{}", response);
//			
//			return ResponseEntity.unprocessableEntity().body(response);
//		}
//		
//		ModelAndView mav = new ModelAndView("forward:/error/errorPage");
//		throw new ModelAndViewDefiningException(mav);
//	}

	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
		log.info("============== MaxUploadSizeExceededException ==============");
		
		ErrorResponse response = ErrorResponse.create().errorCode("error.ExceededSize");
		log.info("{}", response);
		
		return ResponseEntity.badRequest().body(response);
	}
	
	private boolean isAjaxRequest(HttpServletRequest request) {
		String header = request.getHeader("x-requested-with");
		boolean ajax = "XMLHttpRequest".equals(header);
		log.info("## Ajax Request={}", ajax);
		
		return ajax;
	}
	
}
