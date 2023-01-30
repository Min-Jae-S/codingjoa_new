package com.codingjoa.pagination;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Criteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	
	public Criteria() {
		this(1, 10, null, null);
	}

	public Criteria(int page, int recordCnt, String type, String keyword) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
	}
	
	public String getQuery() {
		UriComponentsBuilder builder = UriComponentsBuilder.fromPath("")
				.queryParam("page", this.page)
				.queryParam("recordCnt", this.recordCnt)
				.queryParam("type", this.type)
				.queryParam("keyword", this.keyword);

		return builder.toUriString();
	}
}
