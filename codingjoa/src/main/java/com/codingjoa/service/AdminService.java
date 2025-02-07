package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.BoardInfoDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.entity.Board;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<MemberInfoDto> getPagedMembers();
	
	List<BoardInfoDto> getPagedBoards();

	List<CommentInfoDto> getPagedComments();
	
	Pagination getMemberPagination();

	Pagination getBoardPagination();

	Pagination getCommentPagination();
	
	List<Board> deleteBoards(List<Integer> boardIds);
}
