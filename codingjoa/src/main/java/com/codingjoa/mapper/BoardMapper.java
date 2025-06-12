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

	Map<String, Object> findBoardDetailsById(@Param("boardId") Long boardId, @Param("userId") Long userId);

	void increaseViewCount(Long boardId);

	List<Map<String, Object>> findPagedBoards(@Param("categoryCode") int categoryCode, 
			@Param("boardCri") BoardCriteria boardCri, @Param("userId") Long userId);

	int findTotalCntForPaging(@Param("categoryCode") int categoryCode, @Param("boardCri") BoardCriteria boardCri);

	Board findBoardById(Long boardId);

	boolean updateBoard(Board board);

	boolean deleteBoard(Board board);
	
	void updateCommentCount(@Param("count") int count, @Param("id") Long boardId);
	
	void increaseCommentCount(Long boardId);
	
	void decreaseCommentCount(Long boardId);
	
	void applyCommentCountDelta(@Param("countDelta") Integer countDelta, @Param("id") Long boardId);
	
	void increaseLikeCount(Long boardId);
	
	void decreaseLikeCount(Long boardId);
	
	void applyLikeCountDelta(@Param("countDelta") Integer countDelta, @Param("id") Long boardId);

}
