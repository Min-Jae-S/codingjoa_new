package com.codingjoa.security.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class LoginDto {

	private String email;
	private String password;
	
}
