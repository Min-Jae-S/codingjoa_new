package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Getter;

@JsonInclude(JsonInclude.Include.NON_EMPTY) // temporary
@Getter
public class JwtResponseDto {
	
	private final String tokenType = "Bearer";
	private String accessToken;
	private String refreshToken;
	
	@Builder
	private JwtResponseDto(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
