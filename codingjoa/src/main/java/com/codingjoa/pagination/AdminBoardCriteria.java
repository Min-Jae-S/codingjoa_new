package com.codingjoa.pagination;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class AdminBoardCriteria {
	
	private int page;
	private int recordCnt;
	private String type;
	private String keyword;
	private List<Integer> categories;
	//private List<Integer> categories = new ArrayList<>();
	
	public AdminBoardCriteria(int page, int recordCnt, String type, String keyword, List<Integer> categories) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.type = type;
		this.keyword = keyword;
		this.categories = categories;
	}
	
	@JsonIgnore
	public String getKeywordRegexp() {
		if (!StringUtils.hasText(keyword)) {
			return null;
		}
		
		return "writer".equals(type) ? keyword : String.join("|", keyword.split("\\s+"));
	}

	@Override
	public String toString() {
		return "AdminBoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", type=" + type + ", keyword="
				+ keyword + ", categories=" + categories + ", getKeywordRegexp()=" + getKeywordRegexp() + "]";
	}

}