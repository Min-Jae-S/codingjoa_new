package com.codingjoa.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.codingjoa.service.TopMenuService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.codingjoa.controller")
public class TopMenuHandler {

	@Autowired
	private TopMenuService categoryService;
	
	@ModelAttribute
	public void getParentCategory(Model model) {
		model.addAttribute("parentCategory", categoryService.getParentCategory());
	}
	
}
