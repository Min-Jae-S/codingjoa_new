package com.codingjoa.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.codingjoa.entity.AdminBoard;
import com.codingjoa.entity.AdminUser;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;

@Mapper
public interface AdminMapper {
	
	List<AdminUser> findPagedUsers(AdminUserCriteria adminUserCri);
	
	int findTotalCntForUserPaging(AdminUserCriteria adminUserCri);
	
	AdminUser findUserById(Long userId);
	
	int deleteUsers(List<Long> userIds);
	
	List<AdminBoard> findPagedBoards(AdminBoardCriteria adminBoardCri);
	
	int findTotalCntForBoardPaging(AdminBoardCriteria adminBoardCri);

	int deleteBoards(List<Long> boardIds);
	
}
