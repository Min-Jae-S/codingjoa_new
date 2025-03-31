package com.codingjoa.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserPasswordDto {
	
	private String newPassword;
	private String confirmPassword;
	
}
