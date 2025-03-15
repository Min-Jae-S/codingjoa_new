package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.codingjoa.entity.Category;
import com.codingjoa.mapper.CategoryMapper;
import com.codingjoa.service.CategoryService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
	
	private final CategoryMapper categoryMapper;
	
	@Override
	public List<Category> getParentCategories() {
		return categoryMapper.findParentCategories();
	}

	@Override
	public List<Category> getCategoriesByParent(int parentCode) {
		return categoryMapper.findCategoriesByParent(parentCode);
	}

	@Override
	public Category getCategoryByCode(int code) {
		return categoryMapper.findCategoryByCode(code);
	}
	
	@Override
	public List<Category> getBoardCategories() {
		return categoryMapper.findBoardCategories();
	}

}
