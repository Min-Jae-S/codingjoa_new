package com.codingjoa.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class MemberDetailsDto {
	
	private Integer memberIdx;
	private String memberId;
	private String memberEmail;
	private String memberZipcode;
	private String memberAddr;
	private String memberAddrDetail;
	private Boolean memberAgree;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private Date regdate;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
	private Date moddate;
	
	private String memberImage;
	private List<Integer> myBoardLikes;
	private List<Integer> myCommentLikes;
}
