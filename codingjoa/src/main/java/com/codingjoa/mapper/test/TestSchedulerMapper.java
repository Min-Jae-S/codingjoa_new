package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestSchedulerData;

@Mapper
public interface TestSchedulerMapper {
	
	int insertSample(TestSchedulerData sample);
	
	List<TestSchedulerData> findSamples();
	
	int deleteSamples();
	
}
