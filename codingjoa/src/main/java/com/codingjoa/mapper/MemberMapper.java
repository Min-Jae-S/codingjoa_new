package com.codingjoa.mapper;

import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.codingjoa.entity.Auth;
import com.codingjoa.entity.Member;
import com.codingjoa.entity.SnsInfo;


@Mapper
public interface MemberMapper {
	
	boolean insertMember(Member member);
	
	boolean insertAuth(Auth auth);
	
	boolean insertSnsInfo(SnsInfo snsInfo);
	
	boolean isNicknameExist(String memberNickname);
	
	Map<String, Object> findMemberInfoByIdx(Integer memberIdx);
	
	Member findMemberByEmail(String memberEmail);
	
	Member findMemberByIdx(Integer memberIdx);
	
	boolean updateNickname(Member member);
	
	boolean updateEmail(Member member);
	
	boolean updateAddr(Member member);
	
	boolean updateAgree(Member member);
	
	boolean updatePassword(Member member);
	
	Member findMemeberByIdAndEmail(@Param("memberId") String memberId, @Param("memberEmail") String memberEmail);
	
	Map<String, Object> findUserDetailsByEmail(String memberEmail); // for authentication in UserDetailsService, OAuth2UserService
	
	Map<String, Object> findUserDetailsByIdx(Integer memberIdx);	// for JWT re-issuance after updating
}
