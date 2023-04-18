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
	
	List<Integer> findMemberIdxByKeyword(String keyword);
	
	List<Map<String, Object>> findPagedBoard(@Param("boardCategoryCode") int boardCategoryCode, 
											 @Param("boardCri") Criteria boardCri);

	int findPagedBoardTotalCnt(@Param("boardCategoryCode") int boardCategoryCode, 
							   @Param("boardCri") Criteria boardCri);
	
	boolean isBoardIdxExist(@Param("boardIdx") int boardIdx, @Param("boardCategoryCode") int boardCategoryCode);
	
	Board findModifyBoard(@Param("boardIdx") int boardId, @Param("boardWriterIdx") int boardWriterIdx);
	
//	boolean isMyBoard(@Param("boardIdx")int boardIdx, @Param("boardWriterIdx") int boardWriterIdx);
	
	boolean updateBoard(Board board);

	int findBoardCategoryCode(int boardIdx);
	
	void deleteBoard(int boardIdx);
	
}
