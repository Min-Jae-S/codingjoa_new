package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.BoardImage;

@Mapper
public interface UploadMapper {

	int insertBoardImage(BoardImage boardImage);
	
	boolean isBoardImageUploaded(int boardImageIdx);
	
	void activateBoardImage(@Param("uploadBoardIdx") int uploadBoardIdx, @Param("uploadIdxList") List<Integer> boardImages);
	
	List<Integer> findUploadIdxList(int uploadBoardIdx);
	
	void deactivateBoardImage(int boardImageIdx);
	
}
