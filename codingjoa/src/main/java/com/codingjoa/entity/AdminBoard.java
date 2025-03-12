package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class AdminBoard {
	
	private Integer boardIdx;
	private Integer boardWriterIdx;
	private String boardTitle;
	private String boardContent;
	private String boardContentText;
	private Integer boardCategoryCode;
	private Integer boardViews;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Category category;
	private User writer;
	private Integer commentCnt;
	private Integer likesCnt;
	
}
