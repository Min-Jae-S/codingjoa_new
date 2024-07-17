package com.codingjoa.security.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//	"id" : 3625815491,
//	"connected_at" : "2024-07-17T05:59:35Z",
//	"properties" : {
//	  "nickname" : "서민재",
//	  "profile_image" : "http://k.kakaocdn.net/dn/cb8Hfu/btsIyFy3H06/So3Sj0T3D9kS7KMaKTpYXK/img_640x640.jpg",
//	  "thumbnail_image" : "http://k.kakaocdn.net/dn/cb8Hfu/btsIyFy3H06/So3Sj0T3D9kS7KMaKTpYXK/img_110x110.jpg"
//	},
//	"kakao_account" : {
//	  "profile_nickname_needs_agreement" : false,
//	  "profile_image_needs_agreement" : false,
//	  "profile" : {
//	    "nickname" : "서민재",
//	    "thumbnail_image_url" : "http://k.kakaocdn.net/dn/cb8Hfu/btsIyFy3H06/So3Sj0T3D9kS7KMaKTpYXK/img_110x110.jpg",
//	    "profile_image_url" : "http://k.kakaocdn.net/dn/cb8Hfu/btsIyFy3H06/So3Sj0T3D9kS7KMaKTpYXK/img_640x640.jpg",
//	    "is_default_image" : false,
//	    "is_default_nickname" : false
//	  }
//	}

@ToString
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoMemberDto {

	private String id;
	
}
