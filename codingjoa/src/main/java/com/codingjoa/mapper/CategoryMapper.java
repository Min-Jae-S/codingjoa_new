package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Category;

@Mapper
public interface CategoryMapper {

	List<Category> findParentCategories();
	
	List<Category> findCategoriesByParent(int parentCode);
	
	Category findCategoryByCode(int code);
	
	//List<Category> findCategoryOfSameParent(int categoryParentCode); // SELF JOIN
	
	List<Category> findBoardCategories();
	
	//String findParentCategoryPath(int categoryCode);
	
}
