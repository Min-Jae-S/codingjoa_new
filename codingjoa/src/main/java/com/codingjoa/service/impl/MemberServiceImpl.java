package com.codingjoa.service.impl;

import java.util.Map;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingjoa.dto.AddrDto;
import com.codingjoa.dto.AgreeDto;
import com.codingjoa.dto.EmailAuthDto;
import com.codingjoa.dto.JoinDto;
import com.codingjoa.dto.MemberInfoDto;
import com.codingjoa.dto.NicknameDto;
import com.codingjoa.dto.PasswordChangeDto;
import com.codingjoa.dto.PasswordDto;
import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {
	
	private final MemberMapper memberMapper;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void save(JoinDto joinDto) {
		String rawPassword = joinDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setMemberPassword(encPassword);
		
		Member member = joinDto.toEntity();
		log.info("\t > convert JoinDto to member entity");
		log.info("\t > member = {}", member);
		
		memberMapper.insertMember(member);
		
		Integer memberIdx = member.getMemberIdx();
		log.info("\t > after inserting member, memberIdx = {}", memberIdx);
		
		if (memberIdx == null) {
			throw new ExpectedException("error.InsertMember");
		}
		
		Auth auth = Auth.builder()
				.memberIdx(memberIdx)
				.memberRole("ROLE_MEMBER")
				.build();
		log.info("\t > create new auth = {}", auth);
		
		memberMapper.insertAuth(auth);
		
		Integer authIdx = auth.getAuthIdx();
		log.info("\t > after inserting auth, authIdx = {}", authIdx);
		
		if (authIdx == null) {
			throw new ExpectedException("error.InsertAuth");
		}
	}
	
	@Override
	public boolean isNicknameExist(String memberNickname) {
		return memberMapper.isNicknameExist(memberNickname);
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
	public MemberInfoDto getMemberInfoByIdx(Integer memberIdx) {
		Map<String, Object> memberInfoMap = memberMapper.findMemberInfoByIdx(memberIdx);
		if (memberInfoMap == null) {
			throw new ExpectedException("error.NotFoundMemberInfo");
		}
		
		return MemberInfoDto.from(memberInfoMap);
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
	public void updateNickname(NicknameDto nicknameDto, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		String memberNickname = nicknameDto.getMemberNickname();
		String currentNickname = member.getMemberNickname();
		if (isNicknameExist(memberNickname) && !memberNickname.equals(currentNickname)) {
			throw new ExpectedException("memberNickname", "error.NicknameExist");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(memberIdx)
				.memberNickname(memberNickname)
				.build();
		memberMapper.updateNickname(modifyMember);
	}

	@Override
	public void updateEmail(EmailAuthDto emailAuthDto, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(memberIdx)
				.memberEmail(emailAuthDto.getMemberEmail())
				.build();
		memberMapper.updateEmail(modifyMember);
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(memberIdx)
				.memberZipcode(addrDto.getMemberZipcode())
				.memberAddr(addrDto.getMemberAddr())
				.memberAddrDetail(addrDto.getMemberAddrDetail())
				.build();
		memberMapper.updateAddr(modifyMember);
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(memberIdx)
				.memberAgree(agreeDto.isMemberAgree())
				.build();
		memberMapper.updateAgree(modifyMember);
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
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		String memberPassword = passwordChangeDto.getMemberPassword();
		String currentPassword = member.getMemberPassword();
		if (passwordEncoder.matches(memberPassword, currentPassword)) {
			throw new ExpectedException("memberPassword", "error.NotCurrentPassword");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(memberIdx)
				.memberPassword(passwordEncoder.encode(memberPassword))
				.build();
		memberMapper.updatePassword(modifyMember);
	}
	
	public UserDetails getUserDetailsByIdx(Integer memberIdx) {
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByIdx(memberIdx);
		if (userDetailsMap == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}

}
