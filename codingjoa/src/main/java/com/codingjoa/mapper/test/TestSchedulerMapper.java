package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestSchedulerData;

@Mapper
public interface TestSchedulerMapper {
	
	int insertSampleData(TestSchedulerData testSchedulerData);
	
	List<TestSchedulerData> findSampleData();
	
	int deleteSampleData();
	
}
