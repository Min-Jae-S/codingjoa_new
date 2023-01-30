package com.codingjoa.pagination;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Criteria {
	
	private int categoryCode;
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public Criteria(int categoryCode, int page, int recordCnt, String type, String keyword) {
		this.categoryCode = categoryCode;
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public String getQuery() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("categoryCode", this.categoryCode)
				.queryParam("page", this.page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword);

		return builder.toUriString();
	}

	

}
