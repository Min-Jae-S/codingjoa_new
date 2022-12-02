package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

@Controller
public class TopMenuController {
	
	@Autowired
	private CategoryService categoryService;
	
	@ResponseBody
	@GetMapping("/category/{categoryParentCode}")
	public List<Category> findCategoryByParent(@PathVariable("categoryParentCode") int categoryParentCode) {
		return categoryService.findCategoryListByParent(categoryParentCode);
	}
}
