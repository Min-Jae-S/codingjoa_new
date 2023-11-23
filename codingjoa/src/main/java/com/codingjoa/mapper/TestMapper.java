package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestVo;

@Mapper
public interface TestMapper {
	
	List<TestVo> select();
	
	int insert(TestVo testVo);
	
	int update(TestVo testVo);
	
	int remove(Integer idx);
}
