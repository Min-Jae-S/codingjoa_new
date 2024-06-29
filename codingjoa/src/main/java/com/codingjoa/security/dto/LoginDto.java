package com.codingjoa.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class LoginDto {

	private String memberId;
	private String memberPassword;
	private String redirectUrl;
	
}
