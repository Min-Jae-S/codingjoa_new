package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import com.codingjoa.entity.Member;

import lombok.Data;

@Data
public class JoinDto {

	private String memberId;
	private String memberPassword;
	private String confirmPassword;
	private String memberEmail;
	private String authCode;

	@NotBlank
	private String memberZipcode;
	
	@NotBlank
	private String memberAddr;
	
	@NotBlank
	private String memberAddrDetail;
	
	private boolean memberAgree;
	
	public Member toEntity() {
		return Member.builder()
				.memberId(this.memberId)
				.memberPassword(this.memberPassword)
				.memberEmail(this.memberEmail)
				.memberZipcode(this.memberZipcode)
				.memberAddr(this.memberAddr)
				.memberAddrDetail(this.memberAddrDetail)
				.memberAgree(this.memberAgree)
				.build();
	}
}
