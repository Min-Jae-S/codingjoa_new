package com.codingjoa.mapper.test;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TestTimeoutMapper {

	// delay procedure
	void invokeDelay();
}
