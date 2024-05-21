package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	List<Category> getParentCategoryList();
	
	List<Category> getCategoryListByParent(int categoryParentCode);
	
	Category getCategory(int categoryCode);
	
	String getCategoryName(int categoryCode);
	
	List<Category> getBoardCategoryList();
	
	boolean isBoardCategoryCode(int categoryCode);
}
