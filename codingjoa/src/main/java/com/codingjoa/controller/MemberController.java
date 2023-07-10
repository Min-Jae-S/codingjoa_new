package com.codingjoa.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
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
import com.codingjoa.dto.SessionDto;
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
	
	/*
	 * findAccountResult
	 * findPassword
	 * findPasswordResult
	 */
	
	@GetMapping("/help/findAccount")
	public String findAccount() {
		log.info("## findAccount");
		return "member/find-account";
	}
	
	@GetMapping("/help/findAccountResult")
	public String findAccountResult(Model model) {
		log.info("## findAccountResult");
		log.info("\t > {}", sessionDto);
		
		String findAccountResult = sessionDto.getFindAccountResult();
		if (findAccountResult == null) {
			model.addAttribute("message", MessageUtils.getMessage("error.NotFindAccount"));
			model.addAttribute("path", "findAccount");
			
			return "member/invalid-access";
		}
		
		model.addAttribute("account", findAccountResult);
		sessionDto.setFindAccountResult(null);
		
		return "member/find-account-result";
	}
	
	@GetMapping("/help/findPassword")
	public String findPassword() {
		log.info("## findPassword");
		return "member/find-password";
	}
	
	@GetMapping("/findPasswordResult")
	public String findPasswordResult(Model model) {
		log.info("## findPasswordResult");
		log.info("\t > {}", sessionDto);
		
		Map<String, Object> resultMap = sessionDto.getFindPasswordResult();
		if (resultMap == null || !(boolean) resultMap.get("result")) {
			model.addAttribute("message", MessageUtils.getMessage("error.NotFindPassword"));
			model.addAttribute("path", "findPassword");
			return "member/invalid-access";
		}
		
		resultMap.put("result", false);
		sessionDto.setFindPasswordResult(resultMap);
		
		return "member/update-password";
	}
	
}
