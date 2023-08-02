package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;

@Mapper
public interface UploadMapper {

	int insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(@Param("boardIdx") int boardIdx, @Param("boardImages") List<Integer> boardImages);
	
	void deactivateBoardImage(int boardImageIdx);
	
}
