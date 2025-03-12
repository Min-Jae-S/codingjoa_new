package com.codingjoa.dto;

import com.codingjoa.entity.User;

import lombok.Data;

@Data
public class JoinDto {

	private String memberEmail;
	private String memberNickname;
	private String memberPassword;
	private String confirmPassword;
	private String authCode;
	private boolean memberAgree;
	
	public User toEntity() {
		return User.builder()
				.memberEmail(this.memberEmail)
				.memberNickname(this.memberNickname)
				.memberPassword(this.memberPassword)
				.memberAgree(this.memberAgree)
				.build();
	}
}
