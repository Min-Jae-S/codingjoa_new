package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.AdminBoard;
import com.codingjoa.entity.AdminComment;
import com.codingjoa.entity.AdminMember;
import com.codingjoa.pagination.AdminBoardCriteria;

@Mapper
public interface AdminMapper {
	
	List<AdminMember> findPagedMembers();

	List<AdminBoard> findPagedBoards(AdminBoardCriteria adminBoardCri);
	
	int findPagedBoardsTotalCnt(AdminBoardCriteria adminBoardCri);

	List<AdminComment> findPagedComments();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
	
}
