package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.ManagedBoardDto;
import com.codingjoa.dto.ManagedCommentDto;
import com.codingjoa.dto.UserInfoDto;
import com.codingjoa.pagination.ManagedBoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<UserInfoDto> getPagedMembers();
	
	List<ManagedBoardDto> getPagedBoards(ManagedBoardCriteria adminBoardCri);

	List<ManagedCommentDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination(ManagedBoardCriteria adminBoardCri);

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
}
