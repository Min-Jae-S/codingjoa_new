package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	public List<Category> getParentCategory();
	
	public List<Category> findCategoryByParent(int categoryCode);
	
}
