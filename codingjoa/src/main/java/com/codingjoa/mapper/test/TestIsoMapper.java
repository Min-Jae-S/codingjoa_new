package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestIsoMapper {

	Integer findCurrentNumber();
	
	List<Integer> findNumbers();
	
	int insertNumber(int num);
	
	int updateCurrentNumber(int num);
	
	void deleteNumbers();
	
	void deleteCurrentNumber();
	
}
