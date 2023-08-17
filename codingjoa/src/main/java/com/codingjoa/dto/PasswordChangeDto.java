package com.codingjoa.dto;

import lombok.Data;

@Data
public class PasswordChangeDto {
	
	private String memberPassword;
	private String confirmPassword;
}
