package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestMember;

@Mapper
public interface TestRestApiMapper {
	
	List<TestMember> findMembers();
	
	TestMember findMemberById(String id);
	
}
