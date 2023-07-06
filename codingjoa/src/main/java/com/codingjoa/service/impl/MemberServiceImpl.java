package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.service.MemberService;
import com.codingjoa.util.MessageUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
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
		log.info("joinDto ==> {}", member);
		
		memberMapper.registerMember(member);
		
		Auth auth = new Auth();
		auth.setMemberId(joinDto.getMemberId());
		auth.setMemberRole("ROLE_MEMBER");
		log.info("{}", auth);
		
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
	public boolean isMyEmail(String memberEmail, String memberId) {
		return memberEmail.equals(memberMapper.findEmailById(memberId));
	}
	
	@Override
	public void updateEmail(String memberEmail, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundMember"));
		}
		
		modifiedMember.setMemberEmail(memberEmail);
		memberMapper.updateEmail(modifiedMember);
	}
	
	@Override
	public void updateAddr(String memberZipcode, String memberAddr, String memberAddrDetail, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundMember"));
		}
		
		modifiedMember.setMemberZipcode(memberZipcode);
		modifiedMember.setMemberAddr(memberAddr);
		modifiedMember.setMemberAddrDetail(memberAddrDetail);
		memberMapper.updateAddr(modifiedMember);
	}

	@Override
	public void updateAgree(boolean memberAgree, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundMember"));
		}
		
		modifiedMember.setMemberAgree(memberAgree);
		memberMapper.updateAgree(modifiedMember);
	}

	@Override
	public boolean isMyPassword(String memberPassword, String memberId) {
		String encodedPassword = memberMapper.findPasswordById(memberId);
		return passwordEncoder.matches(memberPassword, encodedPassword);
	}

	@Override
	public void updatePassword(PasswordDto passwordDto, String memberId) {
		String rawPassword = passwordDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		memberMapper.updatePassword(encPassword, memberId);
	}

	@Override
	public String findAccount(EmailAuthDto emailAuthDto) {
		return memberMapper.findIdbyEmail(emailAuthDto.getMemberEmail());
	}

	@Override
	public boolean isAccountExist(String memberId, String memberEmail) {
		return memberMapper.isAccountExist(memberId, memberEmail);
	}



}
