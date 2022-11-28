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
	
	public List<Category> getCategoryList() {
		return categoryMapper.getCategoryList();
	}
}
