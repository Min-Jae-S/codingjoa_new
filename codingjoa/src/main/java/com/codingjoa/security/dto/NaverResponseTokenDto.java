package com.codingjoa.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//	access_token		string		접근 토큰, 발급 후 expires_in 파라미터에 설정된 시간(초)이 지나면 만료됨
//	refresh_token		string		갱신 토큰, 접근 토큰이 만료될 경우 접근 토큰을 다시 발급받을 때 사용
//	token_type			string		접근 토큰의 타입으로 Bearer와 MAC의 두 가지를 지원
//	expires_in			integer		접근 토큰의 유효 기간(초 단위)
//	error				string		에러 코드
//	error_description	string		에러 메시지

@ToString
@Getter 
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class NaverResponseTokenDto {

	public String accessToken;
	public String refreshToken;
	public String tokenType;
	public Integer expiresIn; 
	public String error;
	public String errorDescription;
	
}
