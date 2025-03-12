package com.codingjoa.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class PasswordChangeDto {
	
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	
}
