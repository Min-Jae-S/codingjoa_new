package com.codingjoa.pagination;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;

@Getter
public class AdminBoardCriteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public AdminBoardCriteria(int page, int recordCnt, String type, String keyword) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public AdminBoardCriteria(int page, int recordCnt) {
		this(page, recordCnt, null, null);
	}
	
	public AdminBoardCriteria() {
		this(1, 10, null, null);
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
		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", getKeywordRegexp()=" + getKeywordRegexp() + "]";
	}

}