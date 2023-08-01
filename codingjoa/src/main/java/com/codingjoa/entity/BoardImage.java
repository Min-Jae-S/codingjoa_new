package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	upload_idx              NUMBER,
	upload_board_idx        NUMBER              NULL,
	upload_file             VARCHAR2(500)       NOT NULL,
	regdate                 DATE                NOT NULL,
*/

@Data
public class BoardImage {
	
	private Integer uploadIdx;
	private Integer uploadBoardIdx;
	private String uploadFile;
	private Date regdate;
	
}
