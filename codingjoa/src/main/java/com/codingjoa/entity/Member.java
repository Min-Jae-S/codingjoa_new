package com.codingjoa.entity;

import java.util.Date;

import lombok.Data;

@Data
public class Member {

	private Long memberIdx;
	private String memberId;
	private String memberPassword;
	private String memberEmail;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private Boolean memberAgree;
	private Date regdate;
	private Date moddate;
	
}
