package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface TopMenuService {
	
	public List<Category> getParentCategory();
	
	public List<Category> findCategoryByParent(int parentCategoryCode);
	
}
