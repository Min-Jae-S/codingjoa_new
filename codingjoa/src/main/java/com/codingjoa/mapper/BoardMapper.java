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

	int findPagedBoardsTotalCnt(@Param("categoryCode") int categoryCode, @Param("boardCri") BoardCriteria boardCri);

	Board findBoardById(Long boardId);

	boolean updateBoard(Board board);

	int findCategoryCodeById(Long boardId);

	boolean deleteBoard(Board board);

}
