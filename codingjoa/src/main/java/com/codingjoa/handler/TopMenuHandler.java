package com.codingjoa.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

@ControllerAdvice(basePackages = "com.codingjoa.controller")
public class TopMenuHandler {

	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("parentCategory")
	public List<Category> findParentCategory() {
		return categoryService.findParentCategory();
	}
	
}
