package com.codingjoa.security.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

//    "token_type":"bearer",
//    "access_token":"${ACCESS_TOKEN}",
//    "expires_in":43199,
//    "refresh_token":"${REFRESH_TOKEN}",
//    "refresh_token_expires_in":5184000,
//    "scope":"account_email profile"

@Getter
//@NoArgsConstructor // for deserializing
//@JsonIgnoreProperties
public class KakaoResponseDto {

	public String tokenType;
	public String accessToken;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	public LocalDateTime expiresIn;
	
	public String idToken;
	
	public String refresh_token;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
	public LocalDateTime refresh_token_expires_in;
	
	public String scope;
}
