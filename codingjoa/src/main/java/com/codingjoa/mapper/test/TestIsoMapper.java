package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestIsoMapper {

	Integer findCurrentNumber();
	
	List<Integer> findNumbers();
	
	void insertNumber(int num);
	
	void updateCurrentNumber(int num);
	
	void deleteNumbers();
	
}
