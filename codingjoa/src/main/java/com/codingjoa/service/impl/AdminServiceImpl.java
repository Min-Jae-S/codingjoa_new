package com.codingjoa.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.mapper.AdminMapper;
import com.codingjoa.service.AdminService;

import lombok.RequiredArgsConstructor;

@SuppressWarnings("unused")
@Transactional
@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {

	private final AdminMapper adminMapper;
	
	@Override
	public List<MemberInfoDto> getAllMembers() {
		return null;
	}

}
