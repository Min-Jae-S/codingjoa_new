package com.codingjoa.dto;

import com.codingjoa.enumclass.Type;

import lombok.Data;

@Data
public class PasswordDto {
	
	private String memberPassword;
	private String confirmPassword;
	private Type type;
	
}
