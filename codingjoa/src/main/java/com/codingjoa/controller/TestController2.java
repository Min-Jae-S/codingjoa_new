package com.codingjoa.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.test.Test;
import com.codingjoa.test.TestValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test2")
@Controller @Validated 
public class TestController2 {
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		log.info("## initBinder");
		binder.addValidators(new TestValidator());
	}
	
	@RequestMapping("/param") 
	public ResponseEntity<Object> test(@BoardCategoryCode @RequestParam int boardCategoryCode) {
		log.info("## test called..");
		log.info("\t > boardCategoryCode = {}", boardCategoryCode);
		
		return ResponseEntity.ok(boardCategoryCode);
	}

	@RequestMapping("/param2") 
	public ResponseEntity<Object> test2(@RequestParam int boardCategoryCode) {
		log.info("## test2 called..");
		log.info("\t > boardCategoryCode = {}", boardCategoryCode);
		
		return ResponseEntity.ok(boardCategoryCode);
	}
	
	@ResponseBody
	@RequestMapping("/yesBindingResult")
	public ResponseEntity<Object> yesBindingResult(@Valid Test test, BindingResult bindingResult)
			throws BindException {
		log.info("## yesBindingResult called..");
		log.info("\t > test = {}", test);
		
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> {
				log.info("\t > {} / {}", fieldError.getField(), fieldError.getCodes()[0]);
			});
			
//			if (bindingResult.hasFieldErrors("param1")) {
//				log.info("## bindingResult hasFieldErrors(param1)");
//				throw new BindException(bindingResult);
//			}
		}
		return ResponseEntity.ok(test);
	}

	@ResponseBody
	@RequestMapping("/yesBindingResult2")
	public ResponseEntity<Object> yesBindingResult2(@Validated @ModelAttribute Test test, BindingResult bindingResult)
			throws BindException {
		log.info("## yesBindingResult2 called..");
		log.info("\t > test = {}", test);
		
		if (bindingResult.hasErrors()) {
			bindingResult.getFieldErrors().forEach(fieldError -> { 
				log.info("\t > {} / {}", fieldError.getField(), fieldError.getCodes()[0]);
			});
		}
		return ResponseEntity.ok(test);
	}
	
	@ResponseBody
	@RequestMapping("/noBindingResult")
	public ResponseEntity<Object> noBindingResult(@Valid @ModelAttribute Test test) {
		log.info("## noBindingResult called..");
		log.info("\t > test = {}", test);
		return ResponseEntity.ok(test);
	}

	@ResponseBody
	@RequestMapping("/noBindingResult2")
	public ResponseEntity<Object> noBindingResult2(@Validated @ModelAttribute Test test) {
		log.info("## noBindingResult2 called..");
		log.info("\t > test = {}", test);
		return ResponseEntity.ok(test);
	}

}
