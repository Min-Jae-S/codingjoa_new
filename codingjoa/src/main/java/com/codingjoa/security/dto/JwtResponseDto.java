package com.codingjoa.security.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class JwtResponseDto {
	
	private String tokenType;
	private String accessToken;
	private String refreshToken;
	private Integer accessTokenExpiresIn;
	private Integer refreshTokenExpiresIn;
	
	@Builder
	private JwtResponseDto(String tokenType, String accessToken, String refreshToken, Integer accessTokenExpiresIn,
			Integer refreshTokenExpiresIn) {
		this.tokenType = tokenType;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
	
	
}
