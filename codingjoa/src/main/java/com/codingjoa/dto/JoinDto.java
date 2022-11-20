package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

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
}
