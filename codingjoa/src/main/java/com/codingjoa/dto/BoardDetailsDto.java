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
	private int boardViews;
	private int boardCategoryCode;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String writerNickname;		// from INNER JOIN with member
	private int commentCnt;				// from LEFT OUTER JOIN with comment
	private int likesCnt;				// from LEFT OUTER JOIN with board_likes
	private boolean isWriter;
	private boolean isLiked;		
	
	@Builder
	private BoardDetailsDto(int boardIdx, String boardTitle, String boardContent, int boardViews, int boardCategoryCode,
			LocalDateTime createdAt, LocalDateTime updatedAt, String writerNickname, int commentCnt, int likesCnt,
			boolean isWriter, boolean isLiked) {
		this.boardIdx = boardIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardViews = boardViews;
		this.boardCategoryCode = boardCategoryCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.writerNickname = writerNickname;
		this.commentCnt = commentCnt;
		this.likesCnt = likesCnt;
		this.isWriter = isWriter;
		this.isLiked = isLiked;
	}
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardDetailsDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent="
				+ escapedBoardContent + ", boardViews=" + boardViews + ", boardCategoryCode=" + boardCategoryCode
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", writerNickname=" + writerNickname
				+ ", commentCnt=" + commentCnt + ", likesCnt=" + likesCnt + ", isWriter=" + isWriter + ", isLiked="
				+ isLiked + "]";
	}
	
	public static BoardDetailsDto from(Map<String, Object> map) {
		return BoardDetailsDto.builder()
				.boardIdx((int) map.get("boardIdx"))
				.boardTitle((String) map.get("boardTitle"))
				.boardContent((String) map.get("boardContent"))
				.boardViews((int) map.get("boardViews"))
				.boardCategoryCode((int) map.get("boardCategoryCode"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.writerNickname((String) map.get("writerNickname"))
				.commentCnt((int) map.get("commentCnt"))
				.likesCnt((int) map.get("likesCnt"))
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
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm");
		return this.createdAt.format(formatter);
	}
	
	private String format(LocalDateTime dateTime) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalDate today = LocalDate.now();
		
		return dateTime.toLocalDate().isEqual(today) ? dateTime.format(timeFormatter) : dateTime.format(dateFormatter);
	}
	
}
