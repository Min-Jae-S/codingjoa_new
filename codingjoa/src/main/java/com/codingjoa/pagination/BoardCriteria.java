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
	
	public BoardCriteria(int page, int recordCnt, String type, String keyword) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public BoardCriteria(int page, int recordCnt) {
		this(page, recordCnt, "title", null);
	}
	
	public String getQueryString() {
		return getQueryString(this.page);
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
		if (!StringUtils.hasText(keyword)) {
			return null;
		}
		
		return "writer".equals(type) ? keyword : String.join("|", keyword.split("\\s+"));
	}

	@Override
	public String toString() {
		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", keywordRegexp=" + getKeywordRegexp() + "]";
	}

}