package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.codingjoa.entity.Test;
import com.codingjoa.response.ErrorResponse;
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
	
	@RequestMapping("/testVoid")
	public void testVoid(Model model) {
		log.info("## testVoid called...");
		model.addAttribute("test", "test");
	}
	
	@RequestMapping("/testView")
	public String testView(Model model) {
		log.info("## testView called...");
		model.addAttribute("test", "test");
		return "test/test-view";
	}
	
	@RequestMapping("/testMavView")
	public ModelAndView testMavView() {
		log.info("## testMavView called...");
		return new ModelAndView("test/test-view");
	}
	
	@RequestMapping("/testForward")
	public String testForward() {
		log.info("## testForward called...");
		return "forward:/test/testView";
	}

	@RequestMapping("/testMavForward")
	public ModelAndView testMavForward() {
		log.info("## testMavForward called...");
		return new ModelAndView("forward:/test/testView");
	}
	
	@RequestMapping("/testRedirect")
	public String testRedirect() {
		log.info("## testRedirect called...");
		return "redirect:/test/testView";
	}

	@RequestMapping("/testMavRedirect")
	public ModelAndView testMavRedirect() {
		log.info("## testMavRedirect called...");
		return new ModelAndView("redirect:/test/testView");
	}

	@ResponseBody
	@RequestMapping("/testString")
	public String testString() {
		log.info("## testString called...");
		return "test";
	}
	
	// return ModelAndView including simple String
	@RequestMapping("/testMavString")
	public ModelAndView testMavString() {
		log.info("## testMavString called...");
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("test");
		
		return mav;
	}

	@ResponseBody
	@RequestMapping("/testJson")
	public ResponseEntity<Object> testJson() {
		log.info("## testJson called...");
		return ResponseEntity.ok().body(
				SuccessResponse.create().message("success.Test").data("test"));
	}

	// return ModelAndView including JSON(Object)
	@RequestMapping("/testMavJson")
	public ModelAndView testMavJson() {
		log.info("## testMavJson called...");
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("test", new Test());
		mav.addObject("successResponse", SuccessResponse.create());
		mav.addObject("errorResponse", ErrorResponse.create());
		
		return mav;
	}
}
