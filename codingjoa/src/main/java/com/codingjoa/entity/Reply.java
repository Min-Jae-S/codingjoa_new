package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
 	reply_idx              NUMBER,
    reply_writer_idx       NUMBER                       NOT NULL,
	reply_board_idx        NUMBER                       NOT NULL,
	reply_content          VARCHAR2(2000)               NOT NULL,
    reply_like             NUMBER           DEFAULT 0   NOT NULL,
	regdate                DATE                         NOT NULL,
    moddate                DATE                         NOT NULL,
*/

@Data
public class Reply {
			
	private Integer replyIdx;
	private Integer replyWriterIdx;
	private Integer replyBoardIdx;
	private String replyContent;
	private Integer replyLike;
	private Date regdate;
	private Date moddate;
	
}
