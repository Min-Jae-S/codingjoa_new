package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	public List<Category> findParentCategory();
	
	public List<Category> findCategoryByParent(int categoryParentCode);
	
	public String findCategoryName(int categoryCode);
}
