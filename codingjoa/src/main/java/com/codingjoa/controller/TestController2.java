package com.codingjoa.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
