package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<AdminUserDto> getPagedUsers(AdminUserCriteria adminUserCri);
	
	List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri);

	Pagination getUserPagination(AdminUserCriteria adminUserCri);

	Pagination getBoardPagination(AdminBoardCriteria adminBoardCri);

	int deleteBoards(List<Long> boardIds);
	
}
