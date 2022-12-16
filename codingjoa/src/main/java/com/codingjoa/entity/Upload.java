package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

/*
	upload_idx              NUMBER,
	upload_member_idx       NUMBER              NOT NULL,
	upload_file             VARCHAR2(500)       NOT NULL,
    upload_use              CHAR(1)             NOT NULL,
	regdate                 DATE                NOT NULL, 
*/

@Data
public class Upload {
	
	private Integer uploadIdx;
	private Integer uploadMemberIdx;
	private String uploadFile;
	private Boolean uploadUse;
	private Date regdate;
	
}
