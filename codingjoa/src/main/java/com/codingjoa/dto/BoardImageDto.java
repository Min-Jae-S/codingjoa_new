package com.codingjoa.dto;

import com.codingjoa.entity.BoardImage;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class BoardImageDto {

	private long id;
	private String path;
	
	@Builder
	private BoardImageDto(long id, String path) {
		this.id = id;
		this.path = path;
	}
	
	public static BoardImageDto from(BoardImage boardImage) {
		return BoardImageDto.builder()
				.id(boardImage.getId())
				.path(boardImage.getPath())
				.build();
	}
}
