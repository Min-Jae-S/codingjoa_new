package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.AdminUserAddrDto;
import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserAuthDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.dto.AdminUserPasswordChangeDto;
import com.codingjoa.dto.AdminUserRegistrationDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<AdminUserDto> getPagedUsers(AdminUserCriteria adminUserCri);
	
	Pagination getUserPagination(AdminUserCriteria adminUserCri);
	
	void updateNickname(NicknameDto nicknameDto, Long userId);
	
	void updateEmail(EmailDto emailDto, Long userId);
	
	void updateAddr(AdminUserAddrDto adminUserAddrDto, Long userId);
	
	void updateAgree(AgreeDto agreeDto, Long userId);
	
	void updateAuth(AdminUserAuthDto adminUserAuthDto, Long userId);
	
	void updatePassword(AdminUserPasswordChangeDto adminUserPasswordChangeDto, Long userId);
	
	void registerUser(AdminUserRegistrationDto adminUserRegistrationDto);
	
	int deleteUsers(List<Long> userIds);
	
	AdminUserDto getAdminUser(Long userId);
	
	List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri);

	Pagination getBoardPagination(AdminBoardCriteria adminBoardCri);

	int deleteBoards(List<Long> boardIds);
	
}
