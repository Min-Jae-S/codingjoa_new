package com.codingjoa.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;

//	{
//	  "access_token" : "${ACCESS_TOKEN}",
//	  "refresh_token" : "${REFRESH_TOKEN}",
//	  "token_type" : "bearer",
//	  "expires_in" : 3600
//	}

@Getter 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NaverTokenResponse {

	public String accessToken;
	public String refreshToken;
	public String tokenType;
	public Integer expiresIn;
	
	@Override
	public String toString() {
		return "(accessToken=" + accessToken + ", refreshToken=" + refreshToken + ", tokenType="
				+ tokenType + ", expiresIn=" + expiresIn + ")";
	} 
	
}
