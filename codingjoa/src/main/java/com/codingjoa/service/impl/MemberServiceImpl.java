package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.JoinDto;
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
	public void checkEmailForJoin(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member != null) {
			throw new ExpectedException(MessageUtils.getMessage("error.EmailExist"));
		}
	}
	
	@Override
	public void checkEmailForUpdate(String memberEmail, Integer memberIdx) {
		Member currentMember = memberMapper.findMemberByIdx(memberIdx);
		String currentEmail = currentMember.getMemberEmail();
		if (memberEmail.equals(currentEmail)) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotMyEmail"));
		}
		
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member != null) {
			throw new ExpectedException(MessageUtils.getMessage("error.EmailExist"));
		}
	}
	
	@Override
	public void checkEmailForReset(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotEmailExist"));
		}
	}
	
	@Override
	public String getMemberIdByEmail(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotEmailExist"));
		}
		
		return member.getMemberId();
	}
	
	@Override
	public Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail) {
		Member member = memberMapper.findMemeberByIdAndEmail(memberId, memberEmail);
		if (member == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotIdOrEmailExist"));
		}
		
		return member.getMemberIdx();
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
	public void checkCurrentPassword(String memberPassword, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundMember"));
		}
		
		String currentPassword = member.getMemberPassword();
		if (!passwordEncoder.matches(memberPassword, currentPassword)) {
			throw new ExpectedException(MessageUtils.getMessage("error.BadCredentials"));
		}
	}

	@Override
	public void updatePassword(String memberPassword, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotFoundMember"));
		}
		
		String currentPassword = modifiedMember.getMemberPassword();
		if (passwordEncoder.matches(memberPassword, currentPassword)) {
			throw new ExpectedException(MessageUtils.getMessage("error.NotCurrentPassword"));
		}
		
		String encPassword = passwordEncoder.encode(memberPassword);
		modifiedMember.setMemberPassword(encPassword);
		memberMapper.updatePassword(modifiedMember);
	}


}
