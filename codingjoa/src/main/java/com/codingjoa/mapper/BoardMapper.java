package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.Upload;

@Mapper
public interface BoardMapper {
	
	int insertUpload(Upload upload);
	
	void insertBoard(Board board);
	
	boolean isTempImageUploaded(int uploadIdx);
	
	void updateTempImage(@Param("boardIdx") int boardIdx, @Param("uploadIdxList") List<Integer> uploadIdxList);
	
	Map<String, Object> findBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<Map<String, Object>> findBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	Board findBoardByIdx(int boardIdx);
	
	List<Integer> findUploadIdxList(int boardIdx);
	
	void updateBoard(Board board);
	
	void updateTempImageUsingMerge(@Param("boardIdx") int boardIdx, @Param("uploadIdx") int uploadIdx);
	
}
