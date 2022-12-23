package com.codingjoa.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;

@Mapper
public interface BoardMapper {
	
	int insertUpload(Upload upload);
	
	void insertBoard(Board board);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void updateTempImage(@Param("boardIdx") int boardIdx, @Param("uploadIdx") int uploadIdx);
	
}
