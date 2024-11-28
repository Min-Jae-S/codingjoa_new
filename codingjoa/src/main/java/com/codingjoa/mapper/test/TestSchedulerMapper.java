package com.codingjoa.mapper.test;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestSchedulerData;

@Mapper
public interface TestSchedulerMapper {
	
	int insert(TestSchedulerData testSchedulerData);
	
}
