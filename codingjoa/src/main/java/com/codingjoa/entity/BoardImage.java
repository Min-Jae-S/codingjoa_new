package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	board_image_idx         NUMBER,
	board_idx               NUMBER              NULL,
	board_image_name        VARCHAR2(500)       NOT NULL,
	regdate                 DATE                NOT NULL,
*/

@Data
public class BoardImage {
	
	private Integer boardImageIdx;
	private Integer boardIdx;
	private String boardImageName;
	private String boardImagePath;
	private Date regdate;
	
	
}
