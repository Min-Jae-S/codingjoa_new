package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
//@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@NoArgsConstructor // requires a default constructor to deserialize an object
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponseTokenDto {

	@JsonProperty("token_type")
	public String tokenType;
	
	@JsonProperty("access_token")
	public String accessToken;
	
	@JsonProperty("expires_in")
	public Integer expiresIn;
	
	@JsonProperty("refresh_token")
	public String refreshToken;
	
	@JsonProperty("refresh_token_expires_in")
	public Integer refreshTokenExpiresIn;
	
	@JsonProperty("scope")
	public String scope;
	
}
