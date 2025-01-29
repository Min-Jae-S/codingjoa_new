package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<MemberInfoDto> getPagedMembers();
	
	List<BoardDetailsDto> getPagedBoards();

	List<CommentDetailsDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination();

	Pagination getCommentPagination();
}
