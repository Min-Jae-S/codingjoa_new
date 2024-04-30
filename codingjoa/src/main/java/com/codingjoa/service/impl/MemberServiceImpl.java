package com.codingjoa.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.service.MemberService;

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
	public void save(JoinDto joinDto) {
		String rawPassword = joinDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setMemberPassword(encPassword);
		
		Member member = modelMapper.map(joinDto, Member.class);
		log.info("\t > convert joinDto to member entity");
		log.info("\t > {}", member);
		memberMapper.saveMember(member);
		
		Auth auth = new Auth(joinDto.getMemberId(), "ROLE_MEMBER");
		log.info("\t > create new auth = {}", auth);
		memberMapper.saveAuth(auth);
	}
	
	@Override
	public boolean isIdExist(String memberId) {
		return memberMapper.isIdExist(memberId);
	}

	@Override
	public void checkEmailForJoin(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member != null) {
			throw new ExpectedException("memberEmail", "error.EmailExist");
		}
	}
	
	@Override
	public void checkEmailForUpdate(String memberEmail, Integer memberIdx) {
		Member currentMember = memberMapper.findMemberByIdx(memberIdx);
		if (currentMember == null) {
			throw new ExpectedException("memberEmail", "error.NotFoundMember");
		}
		
		String currentEmail = currentMember.getMemberEmail();
		if (memberEmail.equals(currentEmail)) {
			throw new ExpectedException("memberEmail", "error.NotCurrentEmail");
		}
		
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member != null) {
			throw new ExpectedException("memberEmail", "error.EmailExist");
		}
	}
	
	@Override
	public void checkEmailForReset(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member == null) {
			throw new ExpectedException("memberEmail", "error.NotEmailExist");
		}
	}
	
	@Override
	public String getMemberIdByEmail(String memberEmail) {
		Member member = memberMapper.findMemberByEmail(memberEmail);
		if (member == null) {
			throw new ExpectedException("memberEmail", "error.NotEmailExist");
		}
		return member.getMemberId();
	}
	
	@Override
	public Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail) {
		Member member = memberMapper.findMemeberByIdAndEmail(memberId, memberEmail);
		if (member == null) {
			throw new ExpectedException("memberEmail", "error.NotIdOrEmailExist");
		}
		return member.getMemberIdx();
	}

	@Override
	public void updateEmail(EmailAuthDto emailAuthDto, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		modifiedMember.setMemberEmail(emailAuthDto.getMemberEmail());
		memberMapper.updateEmail(modifiedMember);
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		modifiedMember.setMemberZipcode(addrDto.getMemberZipcode());
		modifiedMember.setMemberAddr(addrDto.getMemberAddr());
		modifiedMember.setMemberAddrDetail(addrDto.getMemberAddrDetail());
		memberMapper.updateAddr(modifiedMember);
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		modifiedMember.setMemberAgree(agreeDto.isMemberAgree());
		memberMapper.updateAgree(modifiedMember);
	}

	@Override
	public void checkCurrentPassword(PasswordDto passwordDto, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		String memberPassword = passwordDto.getMemberPassword();
		String currentPassword = member.getMemberPassword();
		if (!passwordEncoder.matches(memberPassword, currentPassword)) {
			throw new ExpectedException("memberPassword", "error.BadCredentials");
		}
	}

	@Override
	public void updatePassword(PasswordChangeDto passwordChangeDto, Integer memberIdx) {
		Member modifiedMember = memberMapper.findMemberByIdx(memberIdx);
		if (modifiedMember == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		String memberPassword = passwordChangeDto.getMemberPassword();
		String currentPassword = modifiedMember.getMemberPassword();
		if (passwordEncoder.matches(memberPassword, currentPassword)) {
			throw new ExpectedException("memberPassword", "error.NotCurrentPassword");
		}
		
		memberPassword = passwordEncoder.encode(memberPassword);
		modifiedMember.setMemberPassword(memberPassword);
		memberMapper.updatePassword(modifiedMember);
	}

}
