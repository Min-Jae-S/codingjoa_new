package com.codingjoa.dto;

import com.codingjoa.entity.BoardImage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BoardImageDto {

	private int boardImageIdx;
	private String boardImageUrl;
	
	@Builder
	private BoardImageDto(int boardImageIdx, String boardImageUrl) {
		this.boardImageIdx = boardImageIdx;
		this.boardImageUrl = boardImageUrl;
	}
	
	public static BoardImageDto from(BoardImage boardImage) {
		return BoardImageDto.builder()
				.boardImageIdx(boardImage.getBoardImageIdx())
				.boardImageUrl(boardImage.getBoardImageUrl())
				.build();
	}
}
