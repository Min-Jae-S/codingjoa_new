package com.codingjoa.security.dto;

import javax.print.attribute.standard.DateTimeAtCompleted;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import lombok.Getter;
import lombok.ToString;

//	"id":123456789,
//	"connected_at": "2022-04-11T01:45:28Z",
//	"kakao_account": { 
//	    "profile_nickname_needs_agreement	": false,							// 프로필 또는 닉네임 동의항목 필요
//	    "profile_image_needs_agreement	": false, 								// 프로필 또는 프로필 사진 동의항목 필요
//	    "profile": {
//	        "nickname": "홍길동", 												// 프로필 또는 닉네임 동의항목 필요
//			"thumbnail_image_url": "http://yyy.kakao.com/.../img_110x110.jpg",	// 프로필 또는 프로필 사진 동의항목 필요
//	        "profile_image_url": "http://yyy.kakao.com/dn/.../img_640x640.jpg", 
//	        "is_default_image":false,											
//	        "is_default_nickname": false
//	    },
//	    "name_needs_agreement":false, 			// 이름 동의항목 필요
//	    "name":"홍길동",				
//	    "email_needs_agreement":false, 			// 카카오계정(이메일) 동의항목 필요
//	    "is_email_valid": true,   		
//	    "is_email_verified": true,		
//	    "email": "sample@sample.com",	
//	    "age_range_needs_agreement":false,	 	// 연령대 동의항목 필요
//	    "age_range":"20~29",
//	    "birthyear_needs_agreement": false,		// 출생 연도 동의항목 필요
//	    "birthyear": "2002",
//	    "birthday_needs_agreement":false,		// 생일 동의항목 필요
//	    "birthday":"1130",
//	    "birthday_type":"SOLAR",
//	    "gender_needs_agreement":false,			// 성별 동의항목 필요
//	    "gender":"female",
//	    "phone_number_needs_agreement": false,	// 카카오계정(전화번호) 동의항목 필요
//	    "phone_number": "+82 010-1234-5678",   
//	    "ci_needs_agreement": false,			// CI(연계정보) 동의항목 필요
//	    "ci": "${CI}",
//	    "ci_authenticated_at": "2019-03-11T11:25:22Z",
//	},
//	"properties":{
//	    "${CUSTOM_PROPERTY_KEY}": "${CUSTOM_PROPERTY_VALUE}",
//	    ...
//	},
//	"for_partner": {
//	    "uuid": "${UUID}"
//	}

@ToString
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class KakaoMemberDto {

	private String id;
	
}
