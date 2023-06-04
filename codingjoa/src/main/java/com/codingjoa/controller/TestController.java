package com.codingjoa.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.codingjoa.test.Test;
import com.codingjoa.test.TestEditor;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
@Controller
public class TestController {
	
	@RequestMapping("/test0")
	@ResponseBody
	public Object test0(String param1, String param2) {
		log.info("## test0 called...");
		log.info("\t > param1 = {}, param2 = {}", param1, param2);

		Map<String, String> map = new HashMap<>();
		map.put("param1", param1);
		map.put("param2", param2);
		return map;
	}
	
	@RequestMapping("/test1")
	public String test1(String param1) {
		log.info("## test1 called...");
		log.info("\t > param1 = {}", param1);
		
		return "forward:/test/test2";
	}
	
	@ResponseBody
	@RequestMapping("/test2")
	public String test2() {
		log.info("## test2 called...");
		return "test2";
	}
	
	@RequestMapping("/test3")
	public void test3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("## test3 called...");
		request.getRequestDispatcher("/test/test2").forward(request, response);
		
		/*
		 * If the response has already been committed, this method throws an IllegalStateException.
		 * After using this method, the response should be considered to be committed and should not be written to.
		 * ## 응답이 이미 커밋된 후에는 sendError()를 호출할 수 없습니다.
		 */
		try {
			response.sendError(403); 
		} catch (IllegalStateException e) {
			log.info("## response has already been committed");
			log.info("\t > message = {}", e.getMessage());
		}
	}
	
	@RequestMapping("/npe")
	public String npe() {
		log.info("## npe called...");
		throw new NullPointerException();
	}
	
	
	
	
	// ***************************************************************
	// 		ModelAndView / MappingJackson2JsonView / ViewResolver  
	// ***************************************************************
	
	@RequestMapping("/testVoid")
	public void testVoid(Model model) {
		log.info("## testVoid called...");
		
		model.addAttribute("test", "test");
	}
	
	@RequestMapping("/testView")
	public String testView(Model model) {
		log.info("## testView called...");
		model.addAttribute("test", new Test());
		
		return "test/test-view";
	}
	
	@RequestMapping("/testMavView")
	public ModelAndView testMavView(Model model) {
		log.info("## testMavView called...");
		model.addAttribute("test", new Test());
		
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
		
		return ResponseEntity.ok(new Test());
	}

	// return ModelAndView including JSON(Object)
	@RequestMapping("/testMavJson")
	public ModelAndView testMavJson() {
		log.info("## testMavJson called...");
		
		ModelAndView mav = new ModelAndView("jsonView");
		mav.addObject("test", new Test());
		
		return mav;
	}
	
	@RequestMapping("/testNoJsp")
	public String testNoJsp() {
		log.info("## testNoJsp called...");
		
		return "test/testNoJsp";
	}
	
	@RequestMapping("/testNull1")
	public String testNull1() {
		log.info("## testNull1 called...");
		
		return null;
	}
	
	@RequestMapping("/testNull2")
	public ModelAndView testNull2() {
		log.info("## testNull2 called...");
		ModelAndView mav = new ModelAndView();
		mav.setViewName(null);
		
		return mav;
	}

	@RequestMapping("/testNull3")
	public ModelAndView testNull3() {
		log.info("## testNull3 called...");
		ModelAndView mav = new ModelAndView();
		mav.setView(new MappingJackson2JsonView());
		
		return mav;
	}
	
	
	
	// *********************************************************
	// 		Converter, ConversionService, PropertyEditor
	// *********************************************************
	
	@InitBinder("test")
	public void initBinder(WebDataBinder binder) {
		log.info("-------- {} --------", this.getClass().getSimpleName());
		binder.registerCustomEditor(int.class, new TestEditor());
	}
	
	@RequestMapping("/converter")
	public ResponseEntity<Object> testConverter(@ModelAttribute Test test) {
		log.info("## testConverter called..");
		log.info("\t > {}", test);
		
		return ResponseEntity.ok(test);
	}

	@RequestMapping("/modelattribute")
	public ResponseEntity<Object> testModelattribute(@ModelAttribute Test test) {
		log.info("## testModelattribute called..");
		log.info("\t > {}", test);
		
		return ResponseEntity.ok(test);
	}
	
}
