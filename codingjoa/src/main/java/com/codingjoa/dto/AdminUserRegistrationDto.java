package com.codingjoa.dto;

import com.codingjoa.entity.User;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminUserRegistrationDto {
	
	private String email;
	private String nickname;
	private String password;
	private String confirmPassword;
	
	public User toEntity() {
		return User.builder()
				.email(this.email)
				.nickname(this.nickname)
				.password(this.password)
				.agree(false)
				.build();
	}
	
}
