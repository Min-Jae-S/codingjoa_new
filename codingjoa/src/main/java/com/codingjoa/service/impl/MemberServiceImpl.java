package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.service.MemberService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
//@Transactional
@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberMapper memberMapper;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ModelMapper modelMapper;
	
	@Override
	public void register(JoinDto joinDto) {
		String rawPassword = joinDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setMemberPassword(encPassword);
		
		Member member = modelMapper.map(joinDto, Member.class);
		memberMapper.registerMember(member);
		
		Auth auth = new Auth();
		auth.setMemberId(member.getMemberId());
		memberMapper.registerAuth(auth);
	}

	@Override
	public boolean isIdExist(String memberId) {
		return memberMapper.isIdExist(memberId);
	}
	
	@Override
	public boolean isEmailExist(String memberEmail) {
		return memberMapper.isEmailExist(memberEmail);
	}

	@Override
	public void updateEmail(EmailDto emailDto, String memberId) {
		memberMapper.updateEmail(emailDto.getMemberEmail(), memberId);
	}
	
	@Override
	public boolean isMyEmail(String memberEmail, String memberId) {
		return memberEmail.equals(memberMapper.findEmailById(memberId));
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, String memberId) {
		memberMapper.updateAddr(addrDto.getMemberZipcode(), 
				addrDto.getMemberAddr(), addrDto.getMemberAddrDetail(), memberId);
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, String memberId) {
		memberMapper.updateAgree(agreeDto.isMemberAgree(), memberId);
	}

	@Override
	public boolean isMyPassword(String memberPassword, String memberId) {
		return passwordEncoder.matches(memberPassword, memberMapper.findPasswordById(memberId));
	}

	@Override
	public void updatePassword(PasswordDto passwordDto, String memberId) {
		String rawPassword = passwordDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		memberMapper.updatePassword(encPassword, memberId);
	}



}
