package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminCommentDto;
import com.codingjoa.dto.AccountDto;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<AccountDto> getPagedMembers();
	
	List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri);

	List<AdminCommentDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination(AdminBoardCriteria adminBoardCri);

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
	
	int deleteComments(List<Integer> commentIds);
}
