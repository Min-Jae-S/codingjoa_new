package com.codingjoa.entity;

import java.util.Date;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
	board_image_idx         NUMBER,
	board_idx               NUMBER              NULL,
	board_image_name        VARCHAR2(200)       NOT NULL,
	board_image_url         VARCHAR2(200)       NOT NULL,
	regdate                 DATE                NOT NULL,
*/

@NoArgsConstructor
@Data
public class BoardImage {
	
	private Integer boardImageIdx;
	private Integer boardIdx;
	private String boardImageName;
	private String boardImageUrl;
	private Date regdate;
	
	@Builder
	private BoardImage(Integer boardImageIdx, Integer boardIdx, String boardImageName, String boardImageUrl,
			Date regdate) {
		this.boardImageIdx = boardImageIdx;
		this.boardIdx = boardIdx;
		this.boardImageName = boardImageName;
		this.boardImageUrl = boardImageUrl;
		this.regdate = regdate;
	}
	
	
}
