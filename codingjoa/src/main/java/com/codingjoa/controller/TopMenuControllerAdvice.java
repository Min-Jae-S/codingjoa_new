package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice(basePackages = "com.codingjoa.controller")
public class TopMenuControllerAdvice {
	
	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute("parentCategoryList")
	public List<Category> findParentCategoryList() {
		log.info("-------- findParentCategoryList --------");
		return categoryService.findParentCategoryList();
	}
	
	@ResponseBody
	@GetMapping("/category/{categoryParentCode}")
	public List<Category> findCategoryListByParent(@PathVariable("categoryParentCode") int categoryParentCode) {
		log.info("-------- findCategoryListByParent --------");
		return categoryService.findCategoryListByParent(categoryParentCode);
	}
}
