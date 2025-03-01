package com.codingjoa.pagination;

import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;

@Getter
@JsonInclude(value = Include.NON_EMPTY)
public class AdminBoardCriteria {
	
	private int page;
	private int recordCnt;
	private String keyword;
	private String type;
	private String sort;
	private List<Integer> categories;
	
	public AdminBoardCriteria(int page, int recordCnt, String keyword, String type, String sort, List<Integer> categories) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.keyword = keyword;
		this.type = type;
		this.sort = sort;
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
		return "AdminBoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", keyword=" + keyword + ", type="
				+ type + ", sort=" + sort + ", categories=" + categories + ", getKeywordRegexp()=" + getKeywordRegexp()
				+ "]";
	}
	
}