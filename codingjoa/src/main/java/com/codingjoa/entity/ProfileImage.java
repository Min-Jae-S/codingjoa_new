package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	profile_image_idx         NUMBER,
	member_idx                NUMBER              NULL,
	profile_image_name        VARCHAR2(200)       NOT NULL,
	profile_image_path        VARCHAR2(200)       NOT NULL,
	regdate                   DATE                NOT NULL,
*/

@Data
public class ProfileImage {
	
	private Integer profileImageIdx;
	private Integer memberIdx;
	private String profileImageName;
	private String profileImagePath;
	private Date regdate;
}
