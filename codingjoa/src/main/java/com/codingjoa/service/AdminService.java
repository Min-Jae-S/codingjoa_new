package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<MemberInfoDto> getPagedMembers();
	
	List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri);

	List<CommentInfoDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination(AdminBoardCriteria adminBoardCri);

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
}
