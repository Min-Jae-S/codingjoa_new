package com.codingjoa.security.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.ToString;

//	{
//	  "id" : 3625815491,
//	  "connected_at" : "2024-07-17T05:59:35Z",
//	  "properties" : {
//	    "nickname" : "myNickname"
//	  },
//	  "kakao_account" : {
//	    "profile_nickname_needs_agreement" : false,
//	    "profile" : {
//	      "nickname" : "myNickname",
//	      "is_default_nickname" : false
//	    }
//	  }
//	}

@ToString
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoResponseMemberDto {

	@JsonProperty("id")
	private Long id;
	
	@JsonProperty("connected_at")
	private String connectedAt;
	
	private String nickname;
	
	@JsonProperty("kakao_account")
	private void unpackNickname(JsonNode kakaoAccount) {
		this.nickname = kakaoAccount.path("profile").path("nickname").asText();
	}
	
}
