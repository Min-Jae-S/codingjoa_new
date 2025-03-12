package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/*
	member_image_idx         NUMBER,
	member_idx               NUMBER              NOT NULL, --NULL,
	member_image_name        VARCHAR2(200)       NOT NULL,
    member_image_url         VARCHAR2(200)       NOT NULL,
	created_at               DATE                NOT NULL,
*/

@ToString
@Getter
@NoArgsConstructor
public class UserImage {
	
	private Integer memberImageIdx;
	private Integer memberIdx;
	private String memberImageName;
	private String memberImageUrl;
	private LocalDateTime createdAt;
	
	@Builder
	private UserImage(Integer memberImageIdx, Integer memberIdx, String memberImageName, String memberImageUrl) {
		this.memberImageIdx = memberImageIdx;
		this.memberIdx = memberIdx;
		this.memberImageName = memberImageName;
		this.memberImageUrl = memberImageUrl;
	}
}
