package com.codingjoa.dto;

import java.util.List;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserRegistrationDto {
	
	private String email;
	private String nickname;
	private String password;
	private String confirmPassword;
	private List<String> roles;
	
}
