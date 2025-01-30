package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.BoardInfoDto;
import com.codingjoa.dto.CommentDetailsDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.entity.BoardInfo;
import com.codingjoa.mapper.AdminMapper;
import com.codingjoa.pagination.Pagination;
import com.codingjoa.service.AdminService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings("unused")
@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

	private final AdminMapper adminMapper;
	
	@Override
	public List<MemberInfoDto> getPagedMembers() {
		log.info("\t > find pagedMembers");
		return null;
	}
	
	@Override
	public List<BoardInfoDto> getPagedBoards() {
		log.info("\t > find pagedBoards");
		log.info("\t > {}", adminMapper.findPagedBoards());
		log.info("-----------------------------------------------");
		List<BoardInfo> pagedBoards = adminMapper.findPagedBoards();
		pagedBoards.forEach(boardInfo -> log.info("\t > {}", boardInfo));
		return null;
	}

	@Override
	public List<CommentInfoDto> getPagedComments() {
		log.info("\t > find pagedComments");
		return null;
	}

	@Override
	public Pagination getMemberPagination() {
		return null;
	}

	@Override
	public Pagination getBoardPagination() {
		return null;
	}

	@Override
	public Pagination getCommentPagination() {
		return null;
	}

}
