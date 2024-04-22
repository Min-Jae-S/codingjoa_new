package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.LoginDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;
import com.codingjoa.validator.JoinValidator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RedisService redisService;

	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(new JoinValidator(memberService, redisService));
	}

	@GetMapping("/join")
	public String join(@ModelAttribute JoinDto joinDto) {
		log.info("## join");
		log.info("\t > {}", joinDto);
		return "member/join";
	}

	@PostMapping("/joinProc")
	public String joinProc(@ModelAttribute @Valid JoinDto joinDto, BindingResult bindingResult) {
		log.info("## joinProc");
		log.info("\t > {}", joinDto);

		if (bindingResult.hasErrors()) {
			return "member/join";
		}
		
		memberService.save(joinDto);
		redisService.deleteKey(joinDto.getMemberEmail());
		return "member/join-success";
	}

	// GET	: common login
	// POST	: login failure --> forward from LoginFailureHandler
	@RequestMapping("/login") 
	public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request) {
		log.info("## login");
		log.info("\t > {}", loginDto);
		log.info("\t > error = {}", request.getAttribute("errorResponse"));
		return "member/login";
	}
	
	@GetMapping("/account")
	public String account() {
		log.info("## account");
		return "member/account";
	}

	@GetMapping("/account/info")
	public String info() {
		log.info("## info");
		return "member/info";
	}

	@GetMapping("/account/confirmPassword")
	public String confirmPassword() {
		log.info("## confirmPassword");
		return "member/confirm-password";
	}

	@GetMapping("/account/updatePassword")
	public String updatePassword() {
		log.info("## updatePassword");
		return "member/update-password";
	}
	
	@GetMapping("/findAccount")
	public String findAccount() {
		log.info("## findAccount");
		return "member/find-account";
	}
	
	@GetMapping("/findPassword")
	public String findPassword() {
		log.info("## findPassword");
		return "member/find-password";
	}
	
	@GetMapping("/resetPassword")
	public String resetPassword(@RequestParam /* (required = true) */ String key, // pre-check in interceptor 
			Model model) {
		log.info("## resetPassword");
		model.addAttribute("key", key);
		return "member/reset-password";
	}
	
}
