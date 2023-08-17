package com.codingjoa.entity;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 	member_idx              NUMBER,
    member_id               VARCHAR2(50)    UNIQUE  NOT NULL,
    member_password         VARCHAR2(70)            NOT NULL,
    member_email            VARCHAR2(100)   UNIQUE  NOT NULL,
    member_zipcode          CHAR(5)                 NOT NULL,
    member_addr             VARCHAR2(150)           NOT NULL,
    member_addr_detail      VARCHAR2(150)           NOT NULL,
    member_agree            CHAR(1)                 NOT NULL,
    regdate                 DATE                    NOT NULL,
    moddate                 DATE                    NOT NULL,
*/

@Data
public class Member {

	private Integer memberIdx;
	private String memberId;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String memberPassword;
	
	private String memberEmail;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private Boolean memberAgree;
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
	private Date regdate;
	
	@JsonFormat(pattern = "yyyy.MM.dd. HH:mm", timezone = "Asia/Seoul")
	private Date moddate;
	
}
