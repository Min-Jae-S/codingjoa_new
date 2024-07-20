package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//	{
//	  "token_type" : "bearer",
//	  "access_token" : "${ACCESS_TOKEN}",
//	  "expires_in" : 21599,
//	  "refresh_token" : "${REFRESH_TOKEN}",
//	  "refresh_token_expires_in" : 5183999,
//	  "scope" : "profile_image profile_nickname"
//	}

@ToString
@Getter 
//@NoArgsConstructor // requires a default constructor to deserialize an object
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponseTokenDto {

	public String tokenType;
	public String accessToken;
	public Integer expiresIn;
	public String refreshToken;
	public Integer refreshTokenExpiresIn;
	public String scope;
	
}
