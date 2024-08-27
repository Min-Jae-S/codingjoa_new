package com.codingjoa.security.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class LoginDto {

	private String memberEmail;
	private String memberPassword;
	
}
