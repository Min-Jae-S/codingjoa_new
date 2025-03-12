package com.codingjoa.dto;

import java.util.ArrayList;
import java.util.List;

import com.codingjoa.annotation.BoardCategoryCode;
import com.codingjoa.entity.Board;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {
	
	private int id;
	private int userId;
	
	@BoardCategoryCode
	private int categoryCode;
	
	private String title;
	private String content;
	private String searchContent;
	
	
	// cannot deserialize instance of `java.util.ArrayList<java.lang.Integer>` out of VALUE_STRING token;
	// It is used when the data is expected to come primarily in the form of lists or arrays, 
	// but sometimes a single value may be received in the form of an array, and should be handled the same way
	//@JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
	private List<Integer> images = new ArrayList<>();
	
	@Builder
	private BoardDto(int id, int userId, int categoryCode, String title, String content, String searchContent, List<Integer> images) {
		this.id = id;
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.searchContent = searchContent;
		this.categoryCode = categoryCode;
		this.images = images;
	}

	public Board toEntity() {
		return Board.builder()
				.id(this.id)
				.userId(this.userId)
				.categoryCode(this.categoryCode)
				.title(this.title)
				.content(this.content)
				.searchContent(this.searchContent)
				.build();
	}

	public static BoardDto from(Board board) {
		return BoardDto.builder()
				.id(board.getId())
				.userId(board.getUserId())
				.categoryCode(board.getCategoryCode())
				.title(board.getTitle())
				.content(board.getContent())
				.build();
	}
	
	private String escapeContent() {
		return (content != null) ? content.replace("\r\n", "\\r\\n") : null;
	}

	@Override
	public String toString() {
		return "BoardDto [id=" + id + ", userId=" + userId + ", title=" + title + ", content=" + escapeContent()
				+ ", searchContent=" + searchContent + ", categoryCode=" + categoryCode + ", images=" + images + "]";
	}
	
	
}
