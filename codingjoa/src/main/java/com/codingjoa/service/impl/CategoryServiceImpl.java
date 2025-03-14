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
	public Category getCategoryById(int code) {
		return categoryMapper.findCategoryById(code);
	}
	
	@Override
	public String getNameById(int code) {
		return categoryMapper.findNameById(code);
	}

	@Override
	public List<Category> getBoardCategories() {
		return categoryMapper.findBoardCategories();
	}

	@Override
	public boolean isBoardCategoryCode(int code) {
		return categoryMapper.isBoardCategoryCode(code);
	}

}
