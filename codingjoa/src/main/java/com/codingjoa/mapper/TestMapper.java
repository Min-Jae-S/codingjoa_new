package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestVo;

@Mapper
public interface TestMapper {
	
	TestVo select();
	
	void insert(TestVo testVo);
	
	void update(TestVo testVo);
	
	void delete();
}
