package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingjoa.entity.Category;
import com.codingjoa.mapper.TopMenuMapper;
import com.codingjoa.service.TopMenuService;

@Service
public class TopMenuServiceImpl implements TopMenuService {
	
	@Autowired
	TopMenuMapper categoryMapper;
	
	public List<Category> getParentCategory() {
		return categoryMapper.getParentCategory();
	}

	@Override
	public List<Category> findCategoryByParent(int parentCategoryCode) {
		return categoryMapper.findCategoryByParent(parentCategoryCode);
	}
}
