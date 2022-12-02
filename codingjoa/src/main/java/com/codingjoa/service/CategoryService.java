package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	public List<Category> findParentCategoryList();
	
	public List<Category> findCategoryListByParent(int categoryParentCode);
	
	public Category findCategory(int categoryCode);
}
