package com.codingjoa.dto;

import java.time.LocalDateTime;

import com.codingjoa.entity.BoardInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardInfoDto {
	
	// board
	private int boardIdx;
	private String boardTitle;
	private int boardViews;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime createdAt;
	
	@JsonFormat(pattern = "yyyy.MM.dd HH:mm:ss")
	private LocalDateTime updatedAt;
	
	// category
	private int categoryCode;
	private String categoryName;
	
	// member
	private int writerIdx;
	private String writerEmail;
	private String writerNickname;
	
	private int commentCnt;
	private int boardLikesCnt;
	
	@Builder
	private BoardInfoDto(int boardIdx, String boardTitle, int boardViews, LocalDateTime createdAt,
			LocalDateTime updatedAt, int categoryCode, String categoryName, int writerIdx, String writerEmail,
			String writerNickname, int commentCnt, int boardLikesCnt) {
		this.boardIdx = boardIdx;
		this.boardTitle = boardTitle;
		this.boardViews = boardViews;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.categoryCode = categoryCode;
		this.categoryName = categoryName;
		this.writerIdx = writerIdx;
		this.writerEmail = writerEmail;
		this.writerNickname = writerNickname;
		this.commentCnt = commentCnt;
		this.boardLikesCnt = boardLikesCnt;
	}
	
	public static BoardInfoDto from(BoardInfo boardInfo) {
		return BoardInfoDto.builder()
				.boardIdx(boardInfo.getBoard().getBoardIdx())
				.boardTitle(boardInfo.getBoard().getBoardTitle())
				.boardViews(boardInfo.getBoard().getBoardViews())
				.createdAt(boardInfo.getBoard().getCreatedAt())
				.updatedAt(boardInfo.getBoard().getUpdatedAt())
				.categoryCode(boardInfo.getCategory().getCategoryCode())
				.categoryName(boardInfo.getCategory().getCategoryName())
				.writerIdx(boardInfo.getWriter().getMemberIdx())
				.writerEmail(boardInfo.getWriter().getMemberEmail())
				.writerNickname(boardInfo.getWriter().getMemberNickname())
				.commentCnt(boardInfo.getCommentCnt())
				.boardLikesCnt(boardInfo.getBoardLikesCnt())
				.build();
	}
	
}
