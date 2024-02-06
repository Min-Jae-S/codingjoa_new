package com.codingjoa.mapper.test;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestTimeoutMapper {

	void delay(int seconds);
	
	void delay2();
	
}
