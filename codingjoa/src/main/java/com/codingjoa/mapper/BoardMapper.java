package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.pagination.Pagination;

@Mapper
public interface BoardMapper {
	
	void insertBoard(Board board);
	
	Map<String, Object> findBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	int findBoardDetailsListCnt(int categoryCode);
	
	List<Map<String, Object>> findBoardDetailsList(@Param("categoryCode")int categoryCode, 
												   @Param("pagination") Pagination pagination);
	
	boolean isBoardIdxExist(int boardIdx);
	
	Board findBoardByIdx(int boardIdx);
	
	boolean isMyBoard(@Param("boardIdx")int boardIdx, @Param("boardWriterIdx") int boardWriterIdx);
	
	void updateBoard(Board board);
	
}
