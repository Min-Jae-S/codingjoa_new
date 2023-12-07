package com.codingjoa.mapper.test;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestVo;

@Mapper
public interface TestMapper {
	
	List<TestVo> selectAll();
	
	int insert(TestVo testVo);
	
	int update(TestVo testVo);
	
	int remove();
	
	int removeAll();
}
