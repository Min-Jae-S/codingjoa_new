package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminCommentDto;
import com.codingjoa.dto.UserInfoDto;
import com.codingjoa.pagination.ManagedBoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<UserInfoDto> getPagedMembers();
	
	List<AdminBoardDto> getPagedBoards(ManagedBoardCriteria adminBoardCri);

	List<AdminCommentDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination(ManagedBoardCriteria adminBoardCri);

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
}
