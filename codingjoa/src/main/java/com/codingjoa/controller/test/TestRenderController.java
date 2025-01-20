package com.codingjoa.controller.test;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.codingjoa.security.dto.PrincipalDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/test/render")
@Controller
public class TestRenderController {

	@RequestMapping("/test1")
	public String test1(Model model, @AuthenticationPrincipal PrincipalDetails principal) {
		log.info("## test1");
		log.info("\t > authentication = {}", SecurityContextHolder.getContext().getAuthentication().getClass().getSimpleName());
		log.info("\t > principal = {}", principal);
		
		if (principal != null) {
			model.addAttribute("email", principal.getEmail());
			model.addAttribute("nickname", principal.getNickname());
		}
		
		return "template/test-template1";
	}
}
