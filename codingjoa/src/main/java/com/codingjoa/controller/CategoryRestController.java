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

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Category API")
@Slf4j
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@RestController
public class CategoryRestController {
	
	private final CategoryService categoryService;
	
	@GetMapping("/parent/{parentCode}")
	public ResponseEntity<Object> getCategoriesByParent(@PathVariable("parentCode") int parentCode) {
		log.info("## getCategoriesByParent");
		List<Category> categories = categoryService.getCategoriesByParent(parentCode);
		return ResponseEntity.ok(SuccessResponse.builder().data(categories).build());
	}
	
}
