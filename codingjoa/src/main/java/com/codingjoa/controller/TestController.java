package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.response.SuccessResponse;

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
		
		// 응답이 이미 커밋된 후에는 sendError()를 호출할 수 없습니다.
		response.sendError(403); 
	
	}
	
	@RequestMapping("/npe")
	public String npe() {
		log.info("## npe called...");
		throw new NullPointerException();
	}
	
	@RequestMapping("/forward")
	public String forward() {
		log.info("## forward called...");
		return "forward:/test/testView";
	}

	@RequestMapping("/redirect")
	public String redirect() {
		log.info("## redirect called...");
		return "redirect:/test/testView";
	}
	
	@RequestMapping("/testView")
	public String testView() {
		log.info("## testView called...");
		return "test/test-view";
	}

	@RequestMapping("/testVoid")
	public void testVoid() {
		log.info("## testVoid called...");
	}
	
	@ResponseBody
	@RequestMapping("/testString")
	public String testString() {
		log.info("## testString called...");
		return "testString";
	}

	@ResponseBody
	@RequestMapping("/testJson")
	public ResponseEntity<Object> testJson() {
		log.info("## testJson called...");
		return ResponseEntity.ok().body(
				SuccessResponse.create().message("success.Test").data("test data"));
	}
}
