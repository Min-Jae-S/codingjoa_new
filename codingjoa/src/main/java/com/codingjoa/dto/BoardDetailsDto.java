package com.codingjoa.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class BoardDetailsDto {

	private int boardIdx;
	private String boardTitle;
	private String boardContent;
	private int boardViews;
	private int boardCategoryCode;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private String boardWriterNickname;	// from INNER JOIN with member
	private int commentCnt;				// from LEFT OUTER JOIN with comment
	private int boardLikesCnt;			// from LEFT OUTER JOIN with board_likes
	private boolean isBoardWriter;
	private boolean isBoardLiked;		
	
	@Builder
	private BoardDetailsDto(int boardIdx, String boardTitle, String boardContent, int boardViews, int boardCategoryCode,
			LocalDateTime createdAt, LocalDateTime updatedAt, String boardWriterNickname, int commentCnt, int boardLikesCnt,
			boolean isBoardWriter, boolean isBoardLiked) {
		this.boardIdx = boardIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardViews = boardViews;
		this.boardCategoryCode = boardCategoryCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.boardWriterNickname = boardWriterNickname;
		this.commentCnt = commentCnt;
		this.boardLikesCnt = boardLikesCnt;
		this.isBoardWriter = isBoardWriter;
		this.isBoardLiked = isBoardLiked;
	}
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardDetailsDto [boardIdx=" + boardIdx + ", boardTitle=" + boardTitle + ", boardContent="
				+ escapedBoardContent + ", boardViews=" + boardViews + ", boardCategoryCode=" + boardCategoryCode
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", boardWriterNickname="
				+ boardWriterNickname + ", commentCnt=" + commentCnt + ", boardLikesCnt=" + boardLikesCnt
				+ ", isBoardWriter=" + isBoardWriter + ", isBoardLiked=" + isBoardLiked + "]";
	}
	
	@SuppressWarnings("unchecked")
	public static BoardDetailsDto from(Map<String, Object> map, Integer memberIdx) {
		List<Integer> boardLikers = (List<Integer>) map.get("boardLikers");
		log.info("\t > boardLikers = {}", boardLikers);
		
		return BoardDetailsDto.builder()
				.boardIdx((int) map.get("boardIdx"))
				.boardTitle((String) map.get("boardTitle"))
				.boardContent((String) map.get("boardContent"))
				.boardViews((int) map.get("boardViews"))
				.boardCategoryCode((int) map.get("boardCategoryCode"))
				.createdAt((LocalDateTime) map.get("createdAt"))
				.updatedAt((LocalDateTime) map.get("updatedAt"))
				.boardWriterNickname((String) map.get("boardWriterNickname"))
				.commentCnt((int) map.get("commentCnt"))
				.boardLikesCnt((int) map.get("boardLikesCnt"))
				.isBoardWriter(checkBoardWriter(map, memberIdx))
				.isBoardLiked(checkBoardLiked(map, memberIdx))
				.build();
	}
	
	private static boolean checkBoardWriter(Map<String, Object> map, Integer memberIdx) {
		if (memberIdx == null) {
			return false;
		}
		Integer boardWriterIdx = (Integer) map.get("boardWriterIdx");
		return memberIdx.equals(boardWriterIdx);
	}
	
	@SuppressWarnings("unchecked")
	private static boolean checkBoardLiked(Map<String, Object> map, Integer memberIdx) {
		if (memberIdx == null) {
			return false;
		}
		List<Integer> boardLikers = (List<Integer>) map.get("boardLikers");
		return boardLikers.contains(memberIdx);
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
