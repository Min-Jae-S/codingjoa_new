package com.codingjoa.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.entity.Category;
import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.service.CategoryService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/category")
@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/{categoryParentCode}")
	public ResponseEntity<Object> getCategoryListByParent(@PathVariable("categoryParentCode") int categoryParentCode) {
		log.info("## getCategoryListByParent");
		List<Category> categoryList = categoryService.getCategoryListByParent(categoryParentCode);
		return ResponseEntity.ok(SuccessResponse.builder().data(categoryList).build());
	}
	
}
