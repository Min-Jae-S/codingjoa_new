package com.codingjoa.security.dto;

import java.time.LocalDateTime;

import com.codingjoa.deserializer.UnixTimestampDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//    "token_type":"bearer",
//    "access_token":"${ACCESS_TOKEN}",
//    "expires_in":43199,
//    "refresh_token":"${REFRESH_TOKEN}",
//    "refresh_token_expires_in":5184000,
//    "scope":"account_email profile"

@Getter 
@ToString
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
//@NoArgsConstructor 	// requires a default constructor to deserialize an object
//@JsonIgnoreProperties // already DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES enabled
public class KakaoResponseDto {

	public String tokenType;
	public String accessToken;
	
	@JsonDeserialize(using= UnixTimestampDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	public LocalDateTime expiresIn; // timezone issue, shape
	
	public String refreshToken;
	
	@JsonDeserialize(using= UnixTimestampDeserializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	public LocalDateTime refreshTokenExpiresIn;
	
	public String scope;
}
