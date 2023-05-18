package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
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
	public void c(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("## c called...");
		
		request.getRequestDispatcher("/test/a").forward(request, response);
		
		log.info("## after forward");
		response.sendError(403); // 응답이 이미 커밋된 후에는 sendError()를 호출할 수 없습니다.
	}
}
