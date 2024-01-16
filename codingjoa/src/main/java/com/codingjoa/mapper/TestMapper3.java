package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestMapper3 {

	Integer findCurrentNumber();
	
}
