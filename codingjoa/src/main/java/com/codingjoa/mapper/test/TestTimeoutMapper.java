package com.codingjoa.mapper.test;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestTimeoutMapper {

	void delay1(int seconds);
	
}
