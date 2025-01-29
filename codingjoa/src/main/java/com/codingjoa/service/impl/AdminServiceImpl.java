package com.codingjoa.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.BoardDetailsDto;
import com.codingjoa.dto.MemberInfoDto;
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
//		return adminMapper.findPagedMembers()
//				.stream()
//				.map(memberInfoMap -> MemberInfoDto.from(memberInfoMap))
//				.collect(Collectors.toList());
	}
	
	@Override
	public List<BoardDetailsDto> getPagedBoards() {
		log.info("\t > find pagedBoards");
		return null;
//		return adminMapper.findPagedBoards()
//				.stream()
//				.map(boardDetailsMap -> BoardDetailsDto.from(boardDetailsMap))
//				.collect(Collectors.toList());
	}

	@Override
	public Pagination getMemberPagination() {
		return null;
	}

	@Override
	public Pagination getBoardPagination() {
		return null;
	}

}
