package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AdminMapper {
	
	List<Map<String, Object>> findPagedMembers();

	List<Map<String, Object>> findPagedBoards();
	
}
