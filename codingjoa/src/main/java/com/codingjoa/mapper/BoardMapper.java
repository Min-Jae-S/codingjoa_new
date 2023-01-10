package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;

@Mapper
public interface BoardMapper {
	
	void insertBoard(Board board);
	
	Map<String, Object> findBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<Map<String, Object>> findBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	Board findBoardByIdx(int boardIdx);
	
	boolean isMyBoard(@Param("boardIdx")int boardIdx, @Param("boardWriterIdx") int boardWriterIdx);
	
	void updateBoard(Board board);
	
}
