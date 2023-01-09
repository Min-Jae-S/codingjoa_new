package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.Board;

@Mapper
public interface BoardMapper {
	
	void insertBoard(Board board);
	
	Map<String, Object> findBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<Map<String, Object>> findBoardDetailsList(int categoryCode);
	
	boolean isBoardIdxExist(int boardIdx);
	
	Board findBoardByIdx(int boardIdx);
	
	void updateBoard(Board board);
	
}
