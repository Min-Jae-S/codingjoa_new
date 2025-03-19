package com.codingjoa.controller;

import javax.servlet.http.HttpServletRequest;
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
import com.codingjoa.service.UserService;
import com.codingjoa.service.RedisService;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;
import com.codingjoa.validator.JoinValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class UserController {

	private final UserService userService;
	private final RedisService redisService;

	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(new JoinValidator(redisService, userService));
	}

	@GetMapping("/join")
	public String join(@ModelAttribute JoinDto joinDto) {
		log.info("## join");
		log.info("\t > joinDto = {}", joinDto);
		return "user/join";
	}

	@PostMapping("/join")
	public String join(@ModelAttribute @Valid JoinDto joinDto, BindingResult bindingResult, HttpServletRequest request) {
		log.info("## join");
		log.info("\t > joinDto = {}", joinDto);

		if (bindingResult.hasErrors()) {
			return "user/join";
		}
		
		userService.saveUser(joinDto);
		redisService.deleteKey(joinDto.getEmail());
		
		request.setAttribute("message", MessageUtils.getMessage("success.join"));
		request.setAttribute("continueUrl", UriUtils.buildLoginUrl(request, ""));
		
		return "feedback/alert-and-redirect";
	}
	
	@GetMapping("/account")
	public String account() {
		log.info("## account");
		return "user/account";
	}

	@GetMapping("/findPassword")
	public String findPassword() {
		log.info("## findPassword");
		return "user/find-password";
	}
	
	@GetMapping("/resetPassword") // pre-check key parameter in interceptor
	public String resetPassword(@RequestParam String key, Model model) {
		log.info("## resetPassword");
		model.addAttribute("key", key);
		return "user/reset-password";
	}
}
