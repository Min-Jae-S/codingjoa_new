package com.codingjoa.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Board;
import com.codingjoa.pagination.Criteria;

@Mapper
public interface BoardMapper {
	
	boolean insertBoard(Board board);
	
	Map<String, Object> findBoardDetails(@Param("boardIdx") Integer boardIdx, @Param("memberIdx") Integer memberIdx);
	
	void updateBoardViews(Integer boardIdx);
	
	//List<Integer> findMemberIdxByKeyword(String keyword);
	
	List<Map<String, Object>> findPagedBoard(@Param("boardCategoryCode") int boardCategoryCode,
			@Param("boardCri") Criteria boardCri, @Param("memberIdx") Integer memberIdx);

	int findPagedBoardTotalCnt(@Param("boardCategoryCode") int boardCategoryCode, @Param("boardCri") Criteria boardCri);
	
	Board findBoardByIdx(Integer boardIdx);
	
	boolean updateBoard(Board board);

	int findBoardCategoryCodeByIdx(Integer boardIdx);
	
	boolean deleteBoard(Board board);
	
}
