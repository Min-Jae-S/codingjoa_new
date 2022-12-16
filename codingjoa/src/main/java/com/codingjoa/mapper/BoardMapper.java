package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface BoardMapper {
	
	void uploadTempImage(@Param("uploadFilename") String uploadFilename, @Param("memberIdx") int memberIdx);
}
