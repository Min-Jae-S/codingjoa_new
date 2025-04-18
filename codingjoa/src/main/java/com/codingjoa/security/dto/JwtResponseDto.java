package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // temporary
@Getter
public class JwtResponseDto {
	
	private final String tokenType = "bearer";
	private String accessToken;
	private String refreshToken;
	private Integer accessTokenExpiresIn;
	private Integer refreshTokenExpiresIn;
	
	@Builder
	private JwtResponseDto(String accessToken, String refreshToken, Integer accessTokenExpiresIn,
			Integer refreshTokenExpiresIn) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
	
	
}
