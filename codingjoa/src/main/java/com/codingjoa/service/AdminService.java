package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminReplyDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<AdminUserDto> getPagedMembers();
	
	List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri);

	List<AdminReplyDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination(AdminBoardCriteria adminBoardCri);

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
}
