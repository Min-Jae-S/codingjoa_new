package com.codingjoa.dto;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserPasswordChangeDto {
	
	private String newPassword;
	private String confirmPassword;
	
}
