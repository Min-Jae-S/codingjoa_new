package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestApiResponseData;

@Mapper
public interface TestRestApiMapper {
	
	List<TestApiResponseData> findMembers();
	
	TestApiResponseData findMemberById(String id);
	
	TestApiResponseData update(String id);
	
}
