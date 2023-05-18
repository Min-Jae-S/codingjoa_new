package com.codingjoa.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestController {
	
	@RequestMapping("/a")
	public String a() {
		log.info("## a called...");
		
		return "forward:/test/b";
	}
	
	@ResponseBody
	@RequestMapping("/b")
	public String b() {
		log.info("## b called...");
		
		return "b";
	}
	
	@RequestMapping("/c")
	public void c(HttpServletResponse response) {
		log.info("## c called...");
		
		try {
			response.sendError(403);
		} catch (IOException e) {
			log.info(e.getMessage());
		}
	}
}
