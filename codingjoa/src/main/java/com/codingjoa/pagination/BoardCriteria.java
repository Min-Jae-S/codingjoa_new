package com.codingjoa.pagination;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

	public static BoardCriteria create() {
		return new BoardCriteria(1, 5, "title", "");
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
	
	public List<String> getTypes() {
		return StringUtils.hasText(type) ? Arrays.stream(type.split("_")).collect(Collectors.toList()) : List.of();
	}

	@Override
	public String toString() {
		return "BoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword=" + keyword
				+ ", getKeywordRegexp()=" + getKeywordRegexp() + ", getTypes()=" + getTypes() + "]";
	}

}