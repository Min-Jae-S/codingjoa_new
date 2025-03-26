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

	List<AdminBoard> findPagedBoards(AdminBoardCriteria adminBoardCri);
	
	int findTotalCntForUserPaging(AdminUserCriteria adminUserCri);

	int findTotalCntForBoardPaging(AdminBoardCriteria adminBoardCri);
	
	int deleteUsers(List<Long> userIds);

	int deleteBoards(List<Long> boardIds);
	
}
