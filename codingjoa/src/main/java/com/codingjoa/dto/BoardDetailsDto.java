package com.codingjoa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDetailsDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardView;
	private int boardCategoryCode;
	private int commentCnt;
	private int likesCnt;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String writerNickname;
	private boolean isWriter;
	private boolean isLiked;		
	
	@Builder
	private BoardDetailsDto(int boardIdx, String boardTitle, String boardContent, int boardView, int boardCategoryCode,
			int commentCnt, int likesCnt, LocalDateTime createdAt, LocalDateTime updatedAt, String writerNickname,
			boolean isWriter, boolean isLiked) {
		this.boardIdx = boardIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardView = boardView;
		this.boardCategoryCode = boardCategoryCode;
		this.commentCnt = commentCnt;
		this.likesCnt = likesCnt;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.writerNickname = writerNickname;
		this.isWriter = isWriter;
		this.isLiked = isLiked;
	}
	
	public static BoardDetailsDto from(Map<String, Object> map) {
		return BoardDetailsDto.builder()
				.boardIdx((int) map.get("boardIdx"))
				.boardTitle((String) map.get("boardTitle"))
				.boardContent((String) map.get("boardContent"))
				.boardView((int) map.get("boardView"))
				.boardCategoryCode((int) map.get("boardCategoryCode"))
				.commentCnt((int) map.get("commentCnt"))
				.likesCnt((int) map.get("likesCnt"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.writerNickname((String) map.get("writerNickname"))
				.isWriter((boolean) map.get("isWriter"))
				.isLiked((boolean) map.get("isLiked"))
				.build();
	}
	
	public String getCreatedAt() {
		return format(this.createdAt);
	}

	public String getUpdatedAt() {
		return format(this.updatedAt);
	}
	
	public String getFullCreatedAt() {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		return this.createdAt.format(formatter);
	}
	
	private String format(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
		LocalDate today = LocalDate.now();
		
		return dateTime.toLocalDate().isEqual(today) ? dateTime.format(timeFormatter) : dateTime.format(dateFormatter);
	}

	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardDetailsDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent="
				+ escapedBoardContent + ", boardView=" + boardView + ", boardCategoryCode=" + boardCategoryCode
				+ ", commentCnt=" + commentCnt + ", likesCnt=" + likesCnt + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + ", writerNickname=" + writerNickname + ", isWriter=" + isWriter + ", isLiked=" + isLiked
				+ "]";
	}

	
}
