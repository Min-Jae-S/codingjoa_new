package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

@RequestMapping("/topMenu")
@Controller
public class TopMenuController {
	
	@Autowired
	private CategoryService categoryService;
	
	@ResponseBody
	@GetMapping("/{categoryCode}")
	public ResponseEntity<List<Category>> findCategoryByParent(@PathVariable int categoryCode) {
		return ResponseEntity.ok(categoryService.findCategoryByParent(categoryCode));
	}
}
