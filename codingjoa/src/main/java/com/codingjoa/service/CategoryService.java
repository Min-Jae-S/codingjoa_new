package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	List<Category> getParentCategories();
	
	List<Category> getCategoriesByParent(int parentCode);
	
	Category getCategoryById(int code);
	
	String getNameById(int code);
	
	List<Category> getBoardCategories();
	
	boolean isBoardCategoryCode(int code);
}
