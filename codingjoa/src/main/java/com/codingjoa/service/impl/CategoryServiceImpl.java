package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.entity.Category;
import com.codingjoa.mapper.CategoryMapper;
import com.codingjoa.service.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
	
	@Autowired
	CategoryMapper categoryMapper;
	
	public List<Category> findParentCategoryList() {
		return categoryMapper.findParentCategoryList();
	}

	@Override
	public List<Category> findCategoryListByParent(int categoryParentCode) {
		return categoryMapper.findCategoryListByParent(categoryParentCode);
	}

	@Override
	public Category findCategory(int categoryCode) {
		return categoryMapper.findCategory(categoryCode);
	}
}
