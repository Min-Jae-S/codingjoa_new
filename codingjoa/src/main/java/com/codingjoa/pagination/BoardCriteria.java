package com.codingjoa.pagination;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;

@Getter
public class BoardCriteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public BoardCriteria() { }
	
	public BoardCriteria(int page, int recordCnt) {
		this.page = page;
		this.recordCnt = recordCnt;
	}
	
	public BoardCriteria(int page, int recordCnt, String type, String keyword) {
		this(page, recordCnt);
		this.type = type;
		this.keyword = keyword;
	}

	public String getQueryString() {
		return UriComponentsBuilder.newInstance()
				.queryParam("page", this.page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword)
				.toUriString()
				.split("\\?")[1];
	}
	
	public String getQueryString(int page) {
		return UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword)
				.toUriString()
				.split("\\?")[1];
	}
	
	public String getKeywordRegexp() {
		return StringUtils.hasText(keyword) ? String.join("|", keyword.split("\\s+")) : null;
	}

	@Override
	public String toString() {
		return "Criteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", getKeywordRegexp()=" + getKeywordRegexp() + "]";
	}

}