package com.codingjoa.dto;

import lombok.Data;

@Data
public class PasswordSaveDto {
	
	private String newPassword;
	private String confirmPassword;
	
}
