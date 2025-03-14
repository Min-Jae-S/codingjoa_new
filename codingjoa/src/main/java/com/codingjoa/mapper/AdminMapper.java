package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.ManagedBoard;
import com.codingjoa.entity.ManagedComment;
import com.codingjoa.entity.ManagedUser;
import com.codingjoa.pagination.ManagedBoardCriteria;

@Mapper
public interface AdminMapper {
	
	List<ManagedUser> findPagedMembers();

	List<ManagedBoard> findPagedBoards(ManagedBoardCriteria adminBoardCri);
	
	int findPagedBoardsTotalCnt(ManagedBoardCriteria adminBoardCri);

	List<ManagedComment> findPagedComments();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
	
}
