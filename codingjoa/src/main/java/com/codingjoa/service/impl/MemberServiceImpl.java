package com.codingjoa.service.impl;

import java.time.LocalDateTime;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
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
import com.codingjoa.entity.SnsInfo;
import com.codingjoa.exception.ExpectedException;
import com.codingjoa.mapper.MemberMapper;
import com.codingjoa.security.dto.PrincipalDetails;
import com.codingjoa.security.oauth2.OAuth2Attributes;
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
	public void saveMember(JoinDto joinDto) {
		String rawPassword = joinDto.getMemberPassword();
		String encPassword = passwordEncoder.encode(rawPassword);
		joinDto.setMemberPassword(encPassword);
		
		Member member = joinDto.toEntity();
		log.info("\t > convert JoinDto to member entity = {}", member);
		
		boolean isMemberSaved = memberMapper.insertMember(member);
		if (!isMemberSaved) {
			throw new ExpectedException("error.SaveMember");
		}
		
		Auth auth = Auth.builder()
				.memberIdx(member.getMemberIdx())
				.memberRole("ROLE_MEMBER")
				.build();
		log.info("\t > create auth entity = {}", auth);
		
		boolean isAuthSaved = memberMapper.insertAuth(auth);
		if (!isAuthSaved) {
			throw new ExpectedException("error.SaveAuth");
		}
	}
	
	@Override
	public void saveOAuth2Member(OAuth2Attributes oAuth2Attributes) {
		String memberNickname = resolveNickname(oAuth2Attributes.getNickname());
		log.info("\t > resovled nickname = {}", memberNickname);
		
		Member member = Member.builder()
				.memberNickname(memberNickname) 
				.memberEmail(oAuth2Attributes.getEmail())
				.memberAgree(false)
				.build();
		log.info("\t > create member entity = {}", member);
		memberMapper.insertMember(member);
		
		SnsInfo snsInfo = SnsInfo.builder()
				.memberIdx(member.getMemberIdx())
				.snsId(oAuth2Attributes.getId())
				.snsProvider(oAuth2Attributes.getProvider())
				.build();
		log.info("\t > create snsInfo entity = {}", snsInfo);
		memberMapper.insertSnsInfo(snsInfo);

		Auth auth = Auth.builder()
				.memberIdx(member.getMemberIdx())
				.memberRole("ROLE_MEMBER")
				.build();
		log.info("\t > create auth entity = {}", auth);
		memberMapper.insertAuth(auth);
	}
	
	private String resolveNickname(String nickname) {
		final int MAX_NICKNAME_LENGTH = 10;
		final int RANDOM_SUFFIX_LENGTH = 4;
		final int MAX_BASE_NICKNAME_LENGTH = MAX_NICKNAME_LENGTH - RANDOM_SUFFIX_LENGTH;
		
		nickname = nickname.replaceAll("\\s+", ""); // google: MinJae Suh --> MinJaeSuh
		
		if (nickname.length() > MAX_NICKNAME_LENGTH) {
			nickname = nickname.substring(0, MAX_NICKNAME_LENGTH);
		}
		
		if (memberMapper.isNicknameExist(nickname)) {
	        String baseNickname = (nickname.length() > MAX_BASE_NICKNAME_LENGTH) 
	        		? nickname.substring(0, MAX_BASE_NICKNAME_LENGTH) 
	        		: nickname;
	        
			do {
				log.info("\t > create new nickname based on '{}' due to conflict: {}", baseNickname, nickname);
				nickname = baseNickname + RandomStringUtils.randomNumeric(RANDOM_SUFFIX_LENGTH);
			} while (memberMapper.isNicknameExist(nickname)); 
		}
		
		return nickname;
	}
	
	@Override
	public void connectOAuth2Member(OAuth2Attributes oAuth2Attributes, Integer memberIdx) {
		SnsInfo snsInfo = SnsInfo.builder()
				.memberIdx(memberIdx)
				.snsId(oAuth2Attributes.getId())
				.snsProvider(oAuth2Attributes.getProvider())
				.connectedAt(LocalDateTime.now())
				.build();
		log.info("\t > create snsInfo entity = {}", snsInfo);
		memberMapper.insertSnsInfo(snsInfo);
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
		
		boolean isUpdated = memberMapper.updateNickname(modifyMember);
		if (!isUpdated) {
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
		
		boolean isUpdated = memberMapper.updateEmail(modifyMember);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateEmail");
		}
		
		// need update snsInfo
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
		
		boolean isUpdated = memberMapper.updateAddr(modifyMember);
		if (!isUpdated) {
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
		
		boolean isUpdated = memberMapper.updateAgree(modifyMember);
		if (!isUpdated) {
			throw new ExpectedException("error.UpdateAgree");
		}
	}
	
	@Override
	public void updatePassword(PasswordChangeDto passwordChangeDto, Integer memberIdx) {
		Member member = getMemberByIdx(memberIdx);
		String memberPassword = member.getMemberPassword();
		String currentPasswordInput = passwordChangeDto.getCurrentPassword();
		if (!passwordEncoder.matches(currentPasswordInput, memberPassword)) {
			throw new ExpectedException("currentPassword", "error.MismatchPassword");
		}
		
		String newPasswordInput = passwordChangeDto.getNewPassword();
		if (passwordEncoder.matches(newPasswordInput, memberPassword)) {
			throw new ExpectedException("newPassword", "error.SameAsPassword");
		}
		
		Member modifyMember = Member.builder()
				.memberIdx(member.getMemberIdx())
				.memberPassword(passwordEncoder.encode(newPasswordInput))
				.build();
		
		boolean isUpdated = memberMapper.updatePassword(modifyMember);
		if (!isUpdated) {
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
		
		boolean isSaved = memberMapper.updatePassword(modifyMember);
		if (!isSaved) {
			throw new ExpectedException("error.SavePassword");
		}
	}
	
	@Override
	public MemberInfoDto getMemberInfoByIdx(Integer memberIdx) {
		Map<String, Object> memberInfoMap = memberMapper.findMemberInfoByIdx(memberIdx);
		if (memberInfoMap == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		return MemberInfoDto.from(memberInfoMap);
	}
	
	@Override
	public PrincipalDetails getUserDetailsByEmail(String memberEmail) {
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByEmail(memberEmail);
		return (userDetailsMap == null) ? null : PrincipalDetails.from(userDetailsMap);
	}
	
	@Override
	public PrincipalDetails getUserDetailsByIdx(Integer memberIdx) {
		Map<String, Object> userDetailsMap = memberMapper.findUserDetailsByIdx(memberIdx);
		if (userDetailsMap == null) {
			throw new ExpectedException("error.NotFoundMember");
		}
		
		return PrincipalDetails.from(userDetailsMap);
	}

}
