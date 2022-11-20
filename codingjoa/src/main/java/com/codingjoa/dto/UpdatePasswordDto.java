package com.codingjoa.dto;

import lombok.Data;

@Data
public class UpdatePasswordDto {
	
	private String memberPassword;
	private String confirmPassword;
}
