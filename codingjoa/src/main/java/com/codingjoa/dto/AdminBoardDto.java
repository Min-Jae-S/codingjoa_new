package com.codingjoa.dto;

import java.time.LocalDateTime;

import com.codingjoa.entity.AdminBoard;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminBoardDto {
	
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
	private boolean isUpdated;
	
	@Builder
	private AdminBoardDto(int boardIdx, String boardTitle, int boardViews, LocalDateTime createdAt,
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
		this.isUpdated = !createdAt.isEqual(updatedAt);
	}
	
	public static AdminBoardDto from(AdminBoard adminBoard) {
		return AdminBoardDto.builder()
				.boardIdx(adminBoard.getBoardIdx())
				.boardTitle(adminBoard.getBoardTitle())
				.boardViews(adminBoard.getBoardViews())
				.createdAt(adminBoard.getCreatedAt())
				.updatedAt(adminBoard.getUpdatedAt())
				.categoryCode(adminBoard.getCategory().getCategoryCode())
				.categoryName(adminBoard.getCategory().getCategoryName())
				.writerIdx(adminBoard.getWriter().getMemberIdx())
				.writerEmail(adminBoard.getWriter().getMemberEmail())
				.writerNickname(adminBoard.getWriter().getMemberNickname())
				.commentCnt(adminBoard.getCommentCnt())
				.likesCnt(adminBoard.getLikesCnt())
				.build();
	}

	@JsonProperty("isUpdated")
	public boolean isUpdated() {
		return isUpdated;
	}
	
}
