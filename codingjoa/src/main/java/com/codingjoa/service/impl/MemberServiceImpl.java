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
import com.codingjoa.dto.PasswordSaveDto;
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
		
		int memberSaveResult = memberMapper.insertMember(member);
		if (memberSaveResult > 0) {
			log.info("\t > member save successful");
		} else {
			log.info("\t > member save failed");
			throw new ExpectedException("error.SaveMember");
		}
		
		Auth auth = Auth.builder()
				.memberIdx(member.getMemberIdx())
				.memberRole("ROLE_MEMBER")
				.build();
		log.info("\t > create new auth = {}", auth);
		
		int authSaveResult = memberMapper.insertAuth(auth);
		if (authSaveResult > 0) {
			log.info("\t > auth save successful");
		} else {
			log.info("\t > auth save failed");
			throw new ExpectedException("error.SaveAuth");
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
	
//	@Override
//	public String getMemberIdByEmail(String memberEmail) {
//		Member member = memberMapper.findMemberByEmail(memberEmail);
//		if (member == null) {
//			throw new ExpectedException("memberEmail", "error.NotEmailExist");
//		}
//		return member.getMemberId();
//	}
	
	@Override
	public Integer getMemberIdxByIdAndEmail(String memberId, String memberEmail) {
		Member member = memberMapper.findMemeberByIdAndEmail(memberId, memberEmail);
		if (member == null) {
			throw new ExpectedException("memberEmail", "error.NotIdOrEmailExist");
		}
		return member.getMemberIdx();
	}
	
	private Member getMemberByIdx(Integer memberIdx) {
		Member member = memberMapper.findMemberByIdx(memberIdx);
		if (member == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		return member;
	}
	
	@Override
	public void updateNickname(NicknameDto nicknameDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		String currentNickname = member.getMemberNickname();
		String memberNickname = nicknameDto.getMemberNickname();
		
		if (isNicknameExist(memberNickname) && !memberNickname.equals(currentNickname)) {
			throw new ExpectedException("memberNickname", "error.NicknameExist");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberNickname(memberNickname)
				.build();
		
		int result = memberMapper.updateNickname(modifyMember);
		if (result > 0) {
			log.info("\t > nickname update successful");
		} else {
			log.info("\t > nickname update failed");
			throw new ExpectedException("error.UpdateNickname");
		}
	}

	@Override
	public void updateEmail(EmailAuthDto emailAuthDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberEmail(emailAuthDto.getMemberEmail())
				.build();
		
		int result = memberMapper.updateEmail(modifyMember);
		if (result > 0) {
			log.info("\t > email update successful");
		} else {
			log.info("\t > email update failed");
			throw new ExpectedException("error.UpdateEmail");
		}
	}
	
	@Override
	public void updateAddr(AddrDto addrDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberZipcode(addrDto.getMemberZipcode())
				.memberAddr(addrDto.getMemberAddr())
				.memberAddrDetail(addrDto.getMemberAddrDetail())
				.build();
		
		int result = memberMapper.updateAddr(modifyMember);
		if (result > 0) {
			log.info("\t > addr update successful");
		} else {
			log.info("\t > addr update failed");
			throw new ExpectedException("error.UpdateAddr");
		}
	}

	@Override
	public void updateAgree(AgreeDto agreeDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberAgree(agreeDto.isMemberAgree())
				.build();
		
		int result = memberMapper.updateAgree(modifyMember);
		if (result > 0) {
			log.info("\t > agree update successful");
		} else {
			log.info("\t > agree update failed");
			throw new ExpectedException("error.UpdateAgree");
		}
	}
	
	@Override
	public void updatePassword(PasswordChangeDto passwordChangeDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		String memberPassword = member.getMemberPassword();
		String currentPasswordInput = passwordChangeDto.getCurrentPassword();
		if (!passwordEncoder.matches(currentPasswordInput, memberPassword)) {
			throw new ExpectedException("currentPassword", "error.BadCredentials");
		}
		
		String newPasswordInput = passwordChangeDto.getNewPassword();
		if (passwordEncoder.matches(newPasswordInput, memberPassword)) {
			throw new ExpectedException("newPassword", "error.NotCurrentPassword");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberPassword(passwordEncoder.encode(newPasswordInput))
				.build();
		
		int result = memberMapper.updatePassword(modifyMember);
		if (result > 0) {
			log.info("\t > password update successful");
		} else {
			log.info("\t > password update failed");
			throw new ExpectedException("error.UpdatePassword");
		}
	}
	
	@Override
	public void savePassword(PasswordSaveDto passwordSaveDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		String newPassword = passwordSaveDto.getNewPassword();
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberPassword(passwordEncoder.encode(newPassword))
				.build();
		
		int result = memberMapper.updatePassword(modifyMember);
		if (result > 0) {
			log.info("\t > password save successful");
		} else {
			log.info("\t > password save failed");
			throw new ExpectedException("error.SavePassword");
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
	public UserDetails getUserDetailsByIdx(Integer memberIdx) {
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByIdx(memberIdx);
		if (userDetailsMap == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}

}
