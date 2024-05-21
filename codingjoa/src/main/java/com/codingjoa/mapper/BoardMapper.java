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
	
	Map<String, Object> findBoardDetailsByIdx(int boardIdx);
	
	void updateBoardViews(int boardIdx);
	
	List<Integer> findMemberIdxByKeyword(String keyword);
	
	List<Map<String, Object>> findPagedBoard(@Param("boardCategoryCode") int boardCategoryCode, @Param("boardCri") Criteria boardCri);

	int findPagedBoardTotalCnt(@Param("boardCategoryCode") int boardCategoryCode, @Param("boardCri") Criteria boardCri);
	
	Board findBoardByIdx(int boardIdx);
	
	void updateBoard(Board board);

	int findBoardCategoryCodeByIdx(int boardIdx);
	
	void deleteBoard(Board board);
	
}
