package com.codingjoa.controller.test;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestRedirectController {
	
	@GetMapping("/redirect/test1")
	public void test1(HttpServletRequest request, HttpServletResponse response) throws IOException { 
		log.info("## test1");
		response.sendRedirect(request.getContextPath());
	}
	
	@ResponseBody
	@GetMapping("/redirect/test2")
	public ResponseEntity<Object> test2(HttpServletResponse response) { 
		log.info("## test2");
		return ResponseEntity.ok("success");
	}

}
