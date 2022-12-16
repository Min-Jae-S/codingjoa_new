package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;

@Mapper
public interface BoardMapper {
	
	void insertUpload(@Param("uploadFilename") String uploadFilename, 
					  @Param("memberIdx") int memberIdx, 
					  @Param("uploadUse") boolean uploadUse);
	
	void insertBoard(Board board);
	
}
