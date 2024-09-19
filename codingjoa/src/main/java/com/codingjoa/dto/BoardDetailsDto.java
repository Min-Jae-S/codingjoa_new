package com.codingjoa.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.codingjoa.util.DateTimeUtils;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardDetailsDto {

	private int boardIdx;
	private int memberIdx;
	private String boardTitle;
	private String boardContent;
	private int boardViews;
	private int boardCategoryCode;
	private String createdAt;
	private String updatedAt;
	private String memberNickname;		// from INNER JOIN with member
	private int commentCnt;				// from OUTER JOIN with comment
	private int boardLikesCnt;			// from OUTER JOIN with board_likes
	
	@Builder
	private BoardDetailsDto(int boardIdx, int memberIdx, String boardTitle, String boardContent, int boardViews,
			int boardCategoryCode, String createdAt, String updatedAt, String memberNickname, int commentCnt,
			int boardLikesCnt) {
		this.boardIdx = boardIdx;
		this.memberIdx = memberIdx;
		this.boardTitle = boardTitle;
		this.boardContent = boardContent;
		this.boardViews = boardViews;
		this.boardCategoryCode = boardCategoryCode;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.memberNickname = memberNickname;
		this.commentCnt = commentCnt;
		this.boardLikesCnt = boardLikesCnt;
	}
	
	@Override
	public String toString() {
		String escapedBoardContent = (boardContent != null) ? boardContent.replace("\r\n", "\\r\\n") : null;
		return "BoardDetailsDto [boardIdx=" + boardIdx + ", memberIdx=" + memberIdx + ", boardTitle=" + boardTitle
				+ ", boardContent=" + escapedBoardContent + ", boardViews=" + boardViews + ", boardCategoryCode="
				+ boardCategoryCode + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", memberNickname="
				+ memberNickname + ", commentCnt=" + commentCnt + ", boardLikesCnt=" + boardLikesCnt + "]";
	}
	
	public static BoardDetailsDto from(Map<String, Object> map) {
		LocalDateTime createdAt = (LocalDateTime) map.get("createdAt");
		LocalDateTime updatedAt = (LocalDateTime) map.get("updatedAt");
		return BoardDetailsDto.builder()
				.boardIdx((int) map.get("boardIdx"))
				.memberIdx((int) map.get("memberIdx"))
				.boardTitle((String) map.get("boardTitle"))
				.boardContent((String) map.get("boardContent"))
				.boardViews((int) map.get("boardViews"))
				.boardCategoryCode((int) map.get("boardCategoryCode"))
				.createdAt(DateTimeUtils.format(createdAt))
				.updatedAt(DateTimeUtils.format(updatedAt))
				.memberNickname((String) map.get("memberNickname"))
				.commentCnt((int) map.get("commentCnt"))
				.boardLikesCnt((int) map.get("boardLikesCnt"))
				.build();
	}
	
}
