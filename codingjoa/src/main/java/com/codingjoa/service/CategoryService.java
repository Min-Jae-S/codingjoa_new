package com.codingjoa.service;

import java.util.List;

import com.codingjoa.entity.Category;

public interface CategoryService {
	
	List<Category> findParentCategoryList();
	
	List<Category> findCategoryListByParent(int categoryParentCode);
	
	Category findCategory(int categoryCode);
	
	//List<Category> findCategoryOfSameParent(int categoryCode);
	
	List<Category> findBoardCategoryList();
}
