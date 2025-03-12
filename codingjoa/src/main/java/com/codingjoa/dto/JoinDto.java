package com.codingjoa.dto;

import com.codingjoa.entity.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor
public class JoinDto {

	private String email;
	private String nickname;
	private String password;
	private String confirmPassword;
	private String authCode;
	private boolean agree;
	
	public User toEntity() {
		return User.builder()
				.email(this.email)
				.nickname(this.nickname)
				.password(this.password)
				.agree(this.agree)
				.build();
	}
}
