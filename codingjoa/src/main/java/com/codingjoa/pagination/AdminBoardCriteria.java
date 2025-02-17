package com.codingjoa.pagination;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
	
	@JsonIgnore
	public String getKeywordRegexp() {
		return StringUtils.hasText(keyword) ? String.join("|", keyword.split("\\s+")) : null;
	}

	@Override
	public String toString() {
		return "AdminBoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", keywordRegexp=" + getKeywordRegexp() + "]";
	}

}