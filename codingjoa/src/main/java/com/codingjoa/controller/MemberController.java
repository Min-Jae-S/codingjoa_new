package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.LoginDto;
import com.codingjoa.dto.SessionDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/member")
@Controller
public class MemberController {

	@Autowired
	private MemberService memberService;
	
	@Autowired
	private RedisService redisService;

	@Resource(name = "joinValidator")
	private Validator joinValidator;
	
	@Resource(name = "sessionDto")
	@Lazy
	private SessionDto sessionDto;

	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		log.info("## initBinderJoin");
		log.info("\t > target = {} / {}", binder.getObjectName(), binder.getTarget());
		binder.addValidators(joinValidator);
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
		
		memberService.register(joinDto);
		redisService.delete(joinDto.getMemberEmail());
		
		return "member/join-success";
	}

	@RequestMapping("/login")
	public String login(@ModelAttribute LoginDto loginDto, HttpServletRequest request) {
		log.info("## login");
		log.info("\t > {}", loginDto);

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

	@GetMapping("/account/checkPassword")
	public String checkPassword() {
		log.info("## checkPassword");
		return "member/check-password";
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
	public String resetPassword(@RequestParam(name = "key", required = true) String key) {
		log.info("## resetPassword");
		log.info("\t > key = {}", key);
		return "member/reset-password";
	}
	
}
