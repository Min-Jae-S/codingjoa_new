package com.codingjoa.service;

import java.util.List;

import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.pagination.Pagination;

public interface AdminService {
	
	List<MemberInfoDto> getPagedMembers();
	
	Pagination getPagination();

}
