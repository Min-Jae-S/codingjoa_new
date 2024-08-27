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
	public List<Category> getParentCategoryList() {
		return categoryMapper.findParentCategoryList();
	}

	@Override
	public List<Category> getCategoryListByParent(int categoryParentCode) {
		return categoryMapper.findCategoryListByParent(categoryParentCode);
	}

	@Override
	public Category getCategory(int categoryCode) {
		return categoryMapper.findCategory(categoryCode);
	}
	
	@Override
	public String getCategoryName(int categoryCode) {
		return categoryMapper.findCategoryName(categoryCode);
	}

	@Override
	public List<Category> getBoardCategoryList() {
		return categoryMapper.findBoardCategoryList();
	}

	@Override
	public boolean isBoardCategoryCode(int categoryCode) {
		return categoryMapper.isBoardCategoryCode(categoryCode);
	}

}
