package com.codingjoa.dto;

import com.codingjoa.entity.Board;
import com.codingjoa.entity.Category;
import com.codingjoa.entity.Member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@Builder(access = AccessLevel.PRIVATE)
public class BoardInfoDto {
	
	private Board board;
	private Category category;
	private Member writer;
	private int commentCnt;
	private int boardLikesCnt;
	
	@Builder
	private BoardInfoDto(Board board, Category category, Member writer, int commentCnt, int boardLikesCnt) {
		this.board = board;
		this.category = category;
		this.writer = writer;
		this.commentCnt = commentCnt;
		this.boardLikesCnt = boardLikesCnt;
	}
	
}
