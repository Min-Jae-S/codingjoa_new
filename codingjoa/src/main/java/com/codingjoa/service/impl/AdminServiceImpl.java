package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.mapper.AdminMapper;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.AdminUserCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.AdminService;

import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Transactional
@Service
public class AdminServiceImpl implements AdminService {

	private final AdminMapper adminMapper;
	private final int pageRange;
	
	public AdminServiceImpl(AdminMapper adminMapper, 
			@Value("${pagination.pageRange}") int pageRange) {
		this.adminMapper = adminMapper;
		this.pageRange = pageRange;
	}

	@Override
	public List<AdminUserDto> getPagedUsers(AdminUserCriteria adminUserCri) {
		log.info("\t > find pagedUsers");
		return null;
	}
	
	@Override
	public List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri) {
		log.info("\t > find pagedBoards");
		return adminMapper.findPagedBoards(adminBoardCri)
				.stream()
				.map(adminBoard -> {
					log.info("\t\t - {}, {}", adminBoard.getUser(), adminBoard.getCategory());
					return AdminBoardDto.from(adminBoard);
				})
				.collect(Collectors.toList());
	}
	
	@Override
	public Pagination getBoardPagination(AdminBoardCriteria adminBoardCri) {
		int totalCnt = adminMapper.findTotalCntForBoardPaging(adminBoardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminBoardCri.getPage(), adminBoardCri.getRecordCnt(), pageRange) : null;
	}


	@Override
	public Pagination getUserPagination(AdminUserCriteria adminUserCri) {
		int totalCnt = adminMapper.findTotalCntForUserPaging(adminUserCri);
		return null;
		//return (totalCnt > 0) ? new Pagination(totalCnt, adminUserCri.getPage(), adminUserCri.getRecordCnt(), pageRange) : null;
	}


	@Override
	public int deleteBoards(List<Long> boardIds) {
		return adminMapper.deleteBoards(boardIds);
	}


}
