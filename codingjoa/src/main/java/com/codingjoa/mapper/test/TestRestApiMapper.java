package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.test.TestApiRequestData;
import com.codingjoa.test.TestApiResponseData;

@Mapper
public interface TestRestApiMapper {
	
	List<TestApiResponseData> findMembers();
	
	TestApiResponseData findMemberById(String id);
	
	void update(@Param("requestData") TestApiRequestData requestData, @Param("id") String id);
	
	int delete(String id);
	
}
