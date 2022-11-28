package com.codingjoa.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.codingjoa.controller")
public class TopMenuHandler {

	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute
	public void getCategoryList(Model model) {
		List<Category> categoryList = categoryService.getCategoryList();
		log.info("{}", categoryList);
		
		model.addAttribute("categoryList", categoryList);
	}
	
}
