package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.pagination.Criteria;

@Mapper
public interface BoardMapper {
	
	void insertBoard(Board board);
	
	Map<String, Object> findBoardDetails(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<Map<String, Object>> findPagedBoardList(Criteria cri);

	int findPagedBoardListTotalCnt(Criteria cri);
	
	boolean isBoardIdxExist(@Param("boardIdx") int boardIdx, @Param("boardCategoryCode") int boardCategoryCode);
	
	Board findBoardByIdx(int boardIdx);
	
	boolean isMyBoard(@Param("boardIdx")int boardIdx, @Param("boardWriterIdx") int boardWriterIdx);
	
	void updateBoard(Board board);
	
	void deleteBoard(int boardIdx);
	
}
