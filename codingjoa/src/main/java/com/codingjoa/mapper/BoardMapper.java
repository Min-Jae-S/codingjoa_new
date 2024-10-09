package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.pagination.BoardCriteria;

@Mapper
public interface BoardMapper {
	
	boolean insertBoard(Board board);
	
	Map<String, Object> findBoardDetailsByIdx(@Param("boardIdx") int boardIdx, @Param("memberIdx") Integer memberIdx);
	
	void updateBoardViews(int boardIdx);
	
	//List<Integer> findMemberIdxByKeyword(String keyword);
	
	List<Map<String, Object>> findPagedBoard(@Param("boardCategoryCode") int boardCategoryCode,
			@Param("boardCri") BoardCriteria boardCri, @Param("memberIdx") Integer memberIdx);

	int findPagedBoardTotalCnt(@Param("boardCategoryCode") int boardCategoryCode, @Param("boardCri") BoardCriteria boardCri);
	
	Board findBoardByIdx(int boardIdx);
	
	boolean updateBoard(Board board);

	int findBoardCategoryCodeByIdx(int boardIdx);
	
	boolean deleteBoard(Board board);
	
}
