package com.codingjoa.controller;

import javax.validation.Valid;

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
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;
import com.codingjoa.validator.JoinValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/member")
@RequiredArgsConstructor
@Controller
public class MemberController {

	private final MemberService memberService;
	private final RedisService redisService;

	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(new JoinValidator(redisService, memberService));
	}

	@GetMapping("/join")
	public String join(@ModelAttribute JoinDto joinDto) {
		log.info("## join");
		log.info("\t > {}", joinDto);
		return "member/join";
	}

	@PostMapping("/join")
	public String join(@ModelAttribute @Valid JoinDto joinDto, BindingResult bindingResult) {
		log.info("## join");
		log.info("\t > {}", joinDto);

		if (bindingResult.hasErrors()) {
			return "member/join";
		}
		
		memberService.saveMember(joinDto);
		redisService.deleteKey(joinDto.getMemberEmail());
		return "member/join-success";
	}

	@GetMapping("/account")
	public String account() {
		log.info("## account");
		return "member/account";
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
