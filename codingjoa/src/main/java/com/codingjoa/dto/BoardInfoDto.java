package com.codingjoa.dto;

import java.time.LocalDateTime;

import com.codingjoa.entity.BoardInfo;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
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
	private int likesCnt;
	
	@Builder
	private BoardInfoDto(int boardIdx, String boardTitle, int boardViews, LocalDateTime createdAt,
			LocalDateTime updatedAt, int categoryCode, String categoryName, int writerIdx, String writerEmail,
			String writerNickname, int commentCnt, int likesCnt) {
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
		this.likesCnt = likesCnt;
	}
	
	public static BoardInfoDto from(BoardInfo boardInfo) {
		return BoardInfoDto.builder()
				.boardIdx(boardInfo.getBoardIdx())
				.boardTitle(boardInfo.getBoardTitle())
				.boardViews(boardInfo.getBoardViews())
				.createdAt(boardInfo.getCreatedAt())
				.updatedAt(boardInfo.getUpdatedAt())
				.categoryCode(boardInfo.getCategory().getCategoryCode())
				.categoryName(boardInfo.getCategory().getCategoryName())
				.writerIdx(boardInfo.getWriter().getMemberIdx())
				.writerEmail(boardInfo.getWriter().getMemberEmail())
				.writerNickname(boardInfo.getWriter().getMemberNickname())
				.commentCnt(boardInfo.getCommentCnt())
				.likesCnt(boardInfo.getLikesCnt())
				.build();
	}
	
}
