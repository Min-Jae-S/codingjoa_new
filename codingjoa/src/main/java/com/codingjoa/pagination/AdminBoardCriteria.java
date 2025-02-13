package com.codingjoa.pagination;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class AdminBoardCriteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public AdminBoardCriteria() {
		log.info("## AdminBoardCriteria()");
	}
	
	public AdminBoardCriteria(int page, int recordCnt, String type, String keyword) {
		log.info("## AdminBoardCriteria(int page, int recordCnt, String type, String keyword)");
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	@JsonIgnore
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
	
	@JsonIgnore
	public String getKeywordRegexp() {
		return StringUtils.hasText(keyword) ? String.join("|", keyword.split("\\s+")) : null;
	}

	@Override
	public String toString() {
		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword + "]";
//		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
//				+ ", getKeywordRegexp()=" + getKeywordRegexp() + "]";
	}

}