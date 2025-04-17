package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.basic.test.TestVo;

@Mapper
public interface TestOuterMapper {
	
	List<TestVo> selectAll();
	
	int insert(TestVo testVo);
	
	int update(TestVo testVo);
	
	int remove();
	
	int removeAll();
	
}
