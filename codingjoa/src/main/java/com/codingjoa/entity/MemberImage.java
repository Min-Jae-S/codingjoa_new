package com.codingjoa.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
	member_image_idx         NUMBER,
	member_idx               NUMBER              NULL,
	member_image_name        VARCHAR2(200)       NOT NULL,
	member_image_url       	 VARCHAR2(200)       NOT NULL,
	regdate                  DATE                NOT NULL,
*/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberImage {
	
	private Integer memberImageIdx;
	private Integer memberIdx;
	private String memberImageName;
	private String memberImageUrl;
	private Date regdate;
}
