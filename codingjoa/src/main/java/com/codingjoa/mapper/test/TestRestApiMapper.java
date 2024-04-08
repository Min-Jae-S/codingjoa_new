package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestApiData;

@Mapper
public interface TestRestApiMapper {
	
	List<TestApiData> findMembers();
	
	TestApiData findMemberById(String id);
	
}
