package com.codingjoa.pagination;

import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Criteria {
	
	private int boardCategoryCode;
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public Criteria(Criteria criteria) {
		this.boardCategoryCode = criteria.boardCategoryCode;
		this.page = criteria.page;
		this.recordCnt = criteria.recordCnt;
		this.type = criteria.type;
		this.keyword = criteria.keyword;
	}
	
	public String getQueryString() {
		return UriComponentsBuilder.newInstance()
				.queryParam("boardCategoryCode", this.boardCategoryCode)
				.queryParam("page", this.page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword)
				.toUriString();
	}
	
	public String getQueryString(int page) {
		return UriComponentsBuilder.newInstance()
				.queryParam("boardCategoryCode", this.boardCategoryCode)
				.queryParam("page", page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword)
				.toUriString();
	}
	
	public String getKeywordRegexp() {
		return StringUtils.hasText(keyword) ? 
				String.join("|", keyword.split("\\s+")) : null;
	}
}