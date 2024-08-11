package com.codingjoa.dto;

import java.util.List;

import com.codingjoa.entity.Member;

import lombok.Data;

@Data
public class MemberDetailsDto {
	
	private Member member;
	private String memberRole;
	private String memberImageUrl;
	private List<Integer> myBoardLikes;
	private List<Integer> myCommentLikes;
	
}
