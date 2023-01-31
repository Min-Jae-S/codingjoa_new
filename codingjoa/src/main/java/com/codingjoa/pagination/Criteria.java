package com.codingjoa.pagination;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Criteria {
	
	private int boardCategoryCode;
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public Criteria(int boardCategoryCode, int page, int recordCnt, String type, String keyword) {
		this.boardCategoryCode = boardCategoryCode;
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public String makeQueryString(int page) {
		return ServletUriComponentsBuilder.newInstance()
				.queryParam("boardCategoryCode", this.boardCategoryCode)
				.queryParam("page", page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword).toUriString();
	}
}