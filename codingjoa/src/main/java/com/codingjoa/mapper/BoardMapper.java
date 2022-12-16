package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;

@Mapper
public interface BoardMapper {
	
	int insertUpload(Upload upload);
	
	void insertBoard(Board board);
	
}
