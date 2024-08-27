package com.codingjoa.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//	{
//	  "access_token" : "${ACCESS_TOKEN}",
//	  "refresh_token" : "${REFRESH_TOKEN}",
//	  "token_type" : "bearer",
//	  "expires_in" : 3600
//	}

@ToString
@Getter 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NaverTokenResponse {

	public String accessToken;
	public String refreshToken;
	public String tokenType;
	public Integer expiresIn;
	
}
