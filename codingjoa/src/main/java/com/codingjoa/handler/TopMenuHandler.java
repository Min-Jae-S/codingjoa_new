package com.codingjoa.handler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
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
public class TopMenuHandler {

	@Autowired
	private CategoryService categoryService;
	
	@ModelAttribute
	public void getParentCategory(Model model) {
		log.info("============== TopMenuHandler ==============");
		
		model.addAttribute("parentCategory", categoryService.getParentCategory());
	}
	
	@ResponseBody
	@GetMapping("/topMenu/{categoryCode}")
	public ResponseEntity<List<Category>> findCategoryByParent(@PathVariable int categoryCode) {
		return ResponseEntity.ok(categoryService.findCategoryByParent(categoryCode));
	}
	
}
