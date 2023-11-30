package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.test.TestVo;

@Mapper
public interface TestMapper {
	
	List<TestVo> selectAll();
	
	int insert(TestVo testVo);
	
	int insertA1(TestVo testVo);
	
	int insertA2(TestVo testVo);
	
	int update(TestVo testVo);
	
	int remove();
}
