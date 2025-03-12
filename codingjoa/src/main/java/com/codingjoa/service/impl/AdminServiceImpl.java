package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AdminBoardDto;
import com.codingjoa.dto.AdminReplyDto;
import com.codingjoa.dto.AdminUserDto;
import com.codingjoa.mapper.AdminMapper;
import com.codingjoa.pagination.AdminBoardCriteria;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.AdminService;

import lombok.extern.slf4j.Slf4j;

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
	public List<AdminUserDto> getPagedMembers() {
		log.info("\t > find pagedMembers");
		return null;
	}
	
	@Override
	public List<AdminBoardDto> getPagedBoards(AdminBoardCriteria adminBoardCri) {
		log.info("\t > find pagedBoards");
		return adminMapper.findPagedBoards(adminBoardCri)
				.stream()
				.map(adminBoard -> AdminBoardDto.from(adminBoard))
				.collect(Collectors.toList());
	}
	
	@Override
	public Pagination getBoardPagination(AdminBoardCriteria adminBoardCri) {
		int totalCnt = adminMapper.findPagedBoardsTotalCnt(adminBoardCri);
		return (totalCnt > 0) ? new Pagination(totalCnt, adminBoardCri.getPage(), adminBoardCri.getRecordCnt(), pageRange) : null;
	}

	@Override
	public List<AdminReplyDto> getPagedComments() {
		log.info("\t > find pagedComments");
		return null;
	}

	@Override
	public Pagination getMemberPagination() {
		return null;
	}

	@Override
	public Pagination getCommentPagination() {
		return null;
	}

	@Override
	public int deleteBoards(List<Integer> boardIds) {
		return adminMapper.deleteBoards(boardIds);
	}

	@Override
	public int deleteComments(List<Integer> commentIds) {
		return adminMapper.deleteComments(commentIds);
	}

}
