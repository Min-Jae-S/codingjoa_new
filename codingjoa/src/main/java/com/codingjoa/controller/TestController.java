package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;
import org.springframework.web.util.UriComponents;

import com.codingjoa.exception.ExpectedException;
import com.codingjoa.exception.TestException;
import com.codingjoa.response.SuccessResponse;
import com.codingjoa.service.TestTxService;
import com.codingjoa.test.Test;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test")
//@Controller
@RestController
public class TestController {
	
	@RequestMapping("/test0")
	public void test3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		log.info("## test0 called...");
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
	
//	@InitBinder("test")
//	public void initBinder(WebDataBinder binder) {
//		log.info("-------- {} --------", this.getClass().getSimpleName());
//		binder.registerCustomEditor(int.class, new PropertyEditorSupport() {
//
//			private final int DEFAULT_VALUE = 100;
//			
//			@Override
//			public void setAsText(String text) throws IllegalArgumentException {
//				log.info("## {}#setAsText", this.getClass().getSimpleName());
//				log.info("\t > text = {}", text);
//				
//				try {
//					setValue(Integer.parseInt(text));
//				} catch (NumberFormatException e) {
//					log.info("\t > {}", e.getClass().getSimpleName());
//					setValue(DEFAULT_VALUE);
//				}
//			}
//		});
//	}
	
	@ResponseBody
	@RequestMapping("/converter")
	public ResponseEntity<Object> testConverter(@ModelAttribute Test test) {
		log.info("## testConverter called..");
		log.info("\t > test = {}", test);
		
		return ResponseEntity.ok(test);
	}
	
	@RequestMapping("/converter2")
	public ResponseEntity<Object> testConverter2(@RequestParam int param1) {
		log.info("## testConverter2 called..");
		log.info("\t > param1 = {}", param1);
		
		return ResponseEntity.ok(param1);
	}
	

	// *********************************************************
	// 			Transaction
	// *********************************************************
	
	@Autowired
	private TestTxService txService;
	
	@RequestMapping("/tx")
	public ResponseEntity<Object> tx () {
		log.info("## calling doSomething1");
		txService.doSomething1();
		log.info("## calling doSomething2");
		txService.doSomething2();
		log.info("## calling doSomething3");
		txService.doSomething3();
		
		return ResponseEntity.ok("success");
	}
	
	// *********************************************************
	// 	  return ResponseEntity.ok(Object) vs return Object 
	// *********************************************************

	// *********************************************************
	// 	  javascript ajax error 
	// *********************************************************
	
	@ResponseBody
	@GetMapping("/test1")
	public ResponseEntity<Object> test1() {
		log.info("## test1");
		throw new ExpectedException("ERROR");
	}

	@ResponseBody
	@PostMapping("/test2")
	public ResponseEntity<Object> test2(@Valid @RequestBody Test test) {
		log.info("## test2");
		return ResponseEntity.ok("test2");
	}

	@ResponseBody
	@GetMapping("/test4")
	public ResponseEntity<Object> test4() {
		log.info("## test4");
		return ResponseEntity.ok("test4");
	}

	@ResponseBody
	@GetMapping("/test5")
	public ResponseEntity<Object> test5() {
		log.info("## test5");
		return ResponseEntity.ok(SuccessResponse.create().message("test5"));
	}
	
	// *********************************************************
	// 				ServletUriComponentsBuilder
	// *********************************************************
	@ResponseBody
	@GetMapping("/test-uri")
	public void testUri(HttpServletRequest request) {
		log.info("## testUri");
		
		// scheme, host, port, context path 를 재사용
		UriComponents uri1 = ServletUriComponentsBuilder.fromContextPath(request)
		        .path("/accounts").build();
		log.info("\t > uri1 = {}", uri1);

		// scheme, host, port, context path, Servlet prefix 를 재사용
		UriComponents uri2 = ServletUriComponentsBuilder.fromServletMapping(request)
				.path("/accounts").build();
		log.info("\t > uri2 = {}", uri2);
	}
	
	// *********************************************************
	// 	  TestException, TestResponse
	// *********************************************************
	@ResponseBody
	@GetMapping("/test-exception")
	public void testException(@RequestBody @Valid Test test) {
		log.info("## testException");
		throw new TestException();
	}
	
	
	
}
