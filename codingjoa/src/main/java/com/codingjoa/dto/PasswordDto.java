package com.codingjoa.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class PasswordDto {
	
	@NotBlank
	private String memberPassword;
	
}
