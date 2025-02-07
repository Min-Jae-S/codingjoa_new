package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardInfoDto;
import com.codingjoa.dto.CommentInfoDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.entity.Board;
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
		return adminMapper.findPagedBoards()
				.stream()
				.map(boardInfo -> BoardInfoDto.from(boardInfo))
				.collect(Collectors.toList());
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

	@Override
	public List<Board> deleteBoards(List<Integer> boardIds) {
		adminMapper.deleteBoards(boardIds);
		return null;
	}

}
