package com.codingjoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class PasswordResetDto {
	
	private String token;
	private String newPassword;
	private String confirmPassword;
	
}
