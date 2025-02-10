package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardInfoDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<MemberInfoDto> getPagedMembers();
	
	List<BoardInfoDto> getPagedBoards(int page, int recordCnt);

	List<CommentInfoDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination();

	Pagination getCommentPagination();
	
	int deleteBoards(List<Integer> boardIds);
}
