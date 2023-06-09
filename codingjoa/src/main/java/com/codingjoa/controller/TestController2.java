package com.codingjoa.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.test.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test2")
@Controller @Validated
public class TestController2 {
	
	@ResponseBody
	@RequestMapping("/yesBindingResult")
	public ResponseEntity<Object> yesBindingResult(@Valid @ModelAttribute Test test, BindingResult bindingResult)
			throws BindException {
		log.info("## TestController2#yesBindingResult called..");
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
	@RequestMapping("/noBindingResult")
	public ResponseEntity<Object> noBindingResult(@Valid @ModelAttribute Test test) {
		log.info("## TestController2#noBindingResult called..");
		log.info("\t > test = {}", test);
		
		return ResponseEntity.ok(test);
	}

	@ResponseBody
	@RequestMapping("/noBindingResult2")
	public ResponseEntity<Object> noBindingResult2(@Validated @ModelAttribute Test test) {
		log.info("## TestController2#noBindingResult2 called..");
		log.info("\t > test = {}", test);
		
		return ResponseEntity.ok(test);
	}

}
