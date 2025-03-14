package com.codingjoa.pagination;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

@Getter
public class ManagedBoardCriteria {
	
	private int page;
	private int recordCnt;
	private String keyword;
	private String type;
	private String sort;
	private List<Integer> categories;
	
	public ManagedBoardCriteria(int page, int recordCnt, String keyword, String type, String sort, List<Integer> categories) {
		this.page = page;
		this.recordCnt = recordCnt;
		this.keyword = keyword;
		this.type = type;
		this.sort = sort;
		this.categories = categories;
	}
	
	@JsonIgnore
	public String getKeywordRegexp() {
		return "writer".equals(type) ? keyword : String.join("|", keyword.split("\\s+"));
	}
	
	@JsonIgnore
	public String[] getTypes() {
		return StringUtils.hasText(type)? type.split("_") : new String[] {};
	}

	@Override
	public String toString() {
		return "AdminBoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", keyword=" + keyword + ", type="
				+ type + ", sort=" + sort + ", categories=" + categories + ", getKeywordRegexp()=" + getKeywordRegexp()
				+ ", getTypes()=" + Arrays.toString(getTypes()) + "]";
	}
	
}