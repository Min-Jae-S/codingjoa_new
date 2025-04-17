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
import org.springframework.web.bind.annotation.RequestParam;

import com.codingjoa.dto.JoinDto;
import com.codingjoa.service.RedisService;
import com.codingjoa.service.UserService;
import com.codingjoa.util.FormatUtils;
import com.codingjoa.util.MessageUtils;
import com.codingjoa.util.UriUtils;
import com.codingjoa.validator.JoinValidator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Controller
public class MainController {
	
	private final RedisService redisService;
	private final UserService userService;
	
	@InitBinder("joinDto")
	public void initBinderJoin(WebDataBinder binder) {
		binder.addValidators(new JoinValidator(redisService, userService));
	}
	
	@GetMapping
	public String main() {
		log.info("## main");
		return "main";
	}

	@GetMapping("/login") 
	public String login(@RequestParam(name = "continue", required = false) String continueUrl, Model model) {
		log.info("## login");
		log.info("\t > continueUrl = {}", FormatUtils.formatString(continueUrl));
		model.addAttribute("continueUrl", continueUrl);
		return "login";
	}

	@GetMapping("/join")
	public String join(@ModelAttribute JoinDto joinDto) {
		log.info("## join");
		log.info("\t > joinDto = {}", joinDto);
		return "join";
	}

	@PostMapping("/join")
	public String join(@ModelAttribute @Valid JoinDto joinDto, BindingResult bindingResult, HttpServletRequest request) {
		log.info("## join");
		log.info("\t > joinDto = {}", joinDto);

		if (bindingResult.hasErrors()) {
			return "join";
		}
		
		userService.saveUser(joinDto);
		redisService.deleteKey(joinDto.getEmail());
		
		request.setAttribute("message", MessageUtils.getMessage("success.join.join"));
		request.setAttribute("continueUrl", UriUtils.buildLoginUrl(request, ""));
		
		return "feedback/alert-and-redirect";
	}
	
	@GetMapping("/password/find")
	public String findPassword() {
		log.info("## findPassword");
		return "find-password";
	}
	
	@GetMapping("/password/reset") // pre-check key parameter in interceptor
	public String resetPassword(@RequestParam String token, Model model) {
		log.info("## resetPassword");
		model.addAttribute("token", token);
		return "reset-password";
	}

	@GetMapping("/api-docs")
	public String forwardSwaggerUI() {
		log.info("## forwardSwaggerUI");
		return "forward:/swagger-ui.html";
	}
	
}
