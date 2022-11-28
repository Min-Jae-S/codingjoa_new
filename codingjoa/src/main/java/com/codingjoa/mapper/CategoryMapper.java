package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Category;

@Mapper
public interface CategoryMapper {

	public List<Category> findParentCategory();
	
	public List<Category> findCategoryByParent(int categoryCode);
}
