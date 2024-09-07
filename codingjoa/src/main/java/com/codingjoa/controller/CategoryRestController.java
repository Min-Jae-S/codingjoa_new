package com.codingjoa.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingjoa.dto.SuccessResponse;
import com.codingjoa.entity.Category;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/api/category")
@RequiredArgsConstructor
@RestController
public class CategoryRestController {
	
	private final CategoryService categoryService;
	
	@GetMapping("/{categoryParentCode}")
	public ResponseEntity<Object> getCategoryListByParent(@PathVariable("categoryParentCode") int categoryParentCode) {
		log.info("## getCategoryListByParent");
		List<Category> categoryList = categoryService.getCategoryListByParent(categoryParentCode);
		
		//return ResponseEntity.ok(SuccessResponse.builder().data(categoryList).build());
		return ResponseEntity.ok(SuccessResponse.builder().details(categoryList).build());
	}
	
}
