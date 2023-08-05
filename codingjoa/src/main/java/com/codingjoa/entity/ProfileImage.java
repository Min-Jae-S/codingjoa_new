package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	profile_image_idx         NUMBER,
	member_idx                NUMBER              NULL,
	profile_image_name        VARCHAR2(500)       NOT NULL,
	regdate                   DATE                NOT NULL,
*/

@Data
public class ProfileImage {
	
	private Integer profileImageIdx;
	private Integer memberIdx;
	private String profileImageName;
	private Date regdate;
	
}
