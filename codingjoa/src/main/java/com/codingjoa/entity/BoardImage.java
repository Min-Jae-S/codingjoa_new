package com.codingjoa.entity;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
	board_image_idx         NUMBER,
	board_idx               NUMBER              NULL,
	board_image_name        VARCHAR2(200)       NOT NULL,
    board_image_path        VARCHAR2(200)       NOT NULL,
	regdate                 DATE                NOT NULL,
*/

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BoardImage {
	
	private Integer boardImageIdx;
	private Integer boardIdx;
	private String boardImageName;
	private String boardImagePath;
	private Date regdate;
}
