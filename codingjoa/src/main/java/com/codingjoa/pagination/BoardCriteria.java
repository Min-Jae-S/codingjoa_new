package com.codingjoa.pagination;

import java.util.Optional;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;

@Getter
public class BoardCriteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public BoardCriteria(int page, int recordCnt, String type, String keyword) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public String getQueryParams() {
		return getQueryParams(this.page);
	}
	
	public String getQueryParams(int page) {
		return UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParamIfPresent("keyword", Optional.of(this.keyword).filter(keyword -> !keyword.isEmpty()))
				.toUriString()
				.split("\\?")[1];
	}
	
	public String getKeywordRegexp() {
		return "writer".equals(type) ? keyword : String.join("|", keyword.split("\\s+"));
	}
	
	public String[] getTypes() {
		return StringUtils.hasText(type)? type.split("_") : new String[] {};
	}
 	
	public static BoardCriteria create() {
		return new BoardCriteria(1, 5, "title", "");
	}

	@Override
	public String toString() {
		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", keywordRegexp=" + getKeywordRegexp() + "]";
	}

}