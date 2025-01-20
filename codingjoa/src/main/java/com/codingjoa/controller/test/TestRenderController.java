package com.codingjoa.controller.test;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.security.dto.PrincipalDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/render")
@Controller
public class TestRenderController {

	@GetMapping("/test1")
	public String test1(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## test1");
		log.info("\t > principal = {}", principal);
		
		if (principal != null) {
			model.addAttribute("email", principal.getEmail());
			model.addAttribute("nickname", principal.getNickname());
		} else {
			model.addAttribute("email", "no email");
			model.addAttribute("nickname", "no nickname");
		}
		
		return "template/test-template1";
	}
}
