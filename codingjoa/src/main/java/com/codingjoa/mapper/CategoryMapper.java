package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Category;

@Mapper
public interface CategoryMapper {

	List<Category> findParentCategoryList();
	
	List<Category> findCategoryListByParent(int categoryParentCode);
	
	Category findCategory(int categoryCode);
	
	//List<Category> findCategoryOfSameParent(int categoryParentCode); // self join
	
	List<Category> findBoardCategoryList();
	
	String findParentCategoryPath(int categoryCode);
}
