package com.codingjoa.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.LoginDto;
import com.codingjoa.service.MemberService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;

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

	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(joinValidator);
	}

	@GetMapping("/join")
	public String join(@ModelAttribute JoinDto joinDto) {
		log.info("join, {}", joinDto);

		return "member/join";
	}

	@PostMapping("/joinProc")
	public String joinProc(@Valid @ModelAttribute JoinDto joinDto, BindingResult bindingResult) {
		log.info("joinProc, {}", joinDto);

		if (bindingResult.hasErrors()) {
			return "member/join";
		}

		memberService.register(joinDto);
		redisService.delete(joinDto.getMemberEmail());
		
		return "member/join-success";
	}

	@RequestMapping("/login")
	public String login(@ModelAttribute LoginDto loginDto) {
		log.info("login, {}", loginDto);

		return "member/login";
	}

	@GetMapping("/security")
	public String security() {
		return "member/security";
	}

	@GetMapping("/info")
	public String info() {
		return "member/info";
	}

	@GetMapping("/checkPassword")
	public String checkPassword() {
		return "member/check-password";
	}

	@GetMapping("/updatePassword")
	public String updatePassword(HttpSession session, Model model) {
		Object obj = session.getAttribute("CHECK_PASSWORD");
		log.info("updatePassword, CHECK_PASSWORD = {}", obj);
		
		if(obj != null && (boolean) obj) {
			session.removeAttribute("CHECK_PASSWORD");
			return "member/update-password";
		}
		
		model.addAttribute("message", MessageUtils.getMessage("error.NotCheckPassword"));
		
		return "member/not-check-password";
	}
	
	@GetMapping("/findAccount")
	public String findAccount() {
		return "member/find-account";
	}
	
	@GetMapping("/findAccountResult")
	public String findAccountResult(HttpSession session, Model model) {
		Object obj = session.getAttribute("FIND_ACCOUNT");
		log.info("findAccountResult, FIND_ACCOUNT = {}", obj);
		
		if(obj != null) {
			model.addAttribute("find_account", obj);
			session.removeAttribute("FIND_ACCOUNT");
			return "member/find-account-result";
		}
		
		model.addAttribute("message", MessageUtils.getMessage("error.NotFindAccount"));
		
		return "member/not-find-account";
	}
	
	@GetMapping("/findPassword")
	public String findPassword() {
		return "member/find-password";
	}

}
