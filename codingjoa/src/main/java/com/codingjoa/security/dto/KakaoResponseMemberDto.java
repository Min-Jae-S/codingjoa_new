package com.codingjoa.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

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
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoResponseMemberDto {

	private Long id;
	private String connectedAt;
	
}
