package com.codingjoa.dto;

import com.codingjoa.entity.Member;

import lombok.Data;

@Data
public class JoinDto {

	private String memberEmail;
	private String memberNickname;
	private String memberPassword;
	private String confirmPassword;
	private String authCode;
	private boolean memberAgree;
	
	public Member toEntity() {
		return Member.builder()
				.memberEmail(this.memberEmail)
				.memberNickname(this.memberNickname)
				.memberPassword(this.memberPassword)
				.memberAgree(this.memberAgree)
				.build();
	}
}
