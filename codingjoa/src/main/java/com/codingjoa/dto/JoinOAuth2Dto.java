package com.codingjoa.dto;

import com.codingjoa.entity.Member;

import lombok.Data;

@Data
public class JoinOAuth2Dto {

	private String memberEmail;
	private String memberNickname;
	
	public Member toEntity() {
		return Member.builder()
				.memberEmail(this.memberEmail)
				.memberNickname(this.memberNickname)
				.memberAgree(false)
				.build();
	}
}
