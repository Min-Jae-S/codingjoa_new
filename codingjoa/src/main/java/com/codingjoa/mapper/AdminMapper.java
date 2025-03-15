package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.AdminBoard;
import com.codingjoa.entity.AdminComment;
import com.codingjoa.entity.AdminUser;
import com.codingjoa.pagination.ManagedBoardCriteria;

@Mapper
public interface AdminMapper {
	
	List<AdminUser> findPagedMembers();

	List<AdminBoard> findPagedBoards(ManagedBoardCriteria adminBoardCri);
	
	int findPagedBoardsTotalCnt(ManagedBoardCriteria adminBoardCri);

	List<AdminComment> findPagedComments();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
	
}
