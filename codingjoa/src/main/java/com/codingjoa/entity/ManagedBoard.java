package com.codingjoa.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class ManagedBoard {
	
	private Long id;
	private Long userId;
	private Integer categoryCode;
	private String title;
	private String content;
	private String searchContent;
	private Integer viewCount;
	private Integer replyCount;
	private Integer likeCount;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Category category;
	private User writer;
	
}
