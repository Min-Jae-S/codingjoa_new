package com.codingjoa.pagination;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

@Getter
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
	
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	public String getKeywordRegexp() {
		return "writer".equals(type) ? keyword : String.join("|", keyword.split("\\s+"));
	}
	
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	public List<String> getTypes() {
		return StringUtils.hasText(type) ? Arrays.stream(type.split("_")).collect(Collectors.toList()) : List.of();
	}

	@Override
	public String toString() {
		return "AdminBoardCriteria [page=" + page + ", recordCnt=" + recordCnt + ", keyword=" + keyword + ", type="
				+ type + ", sort=" + sort + ", categories=" + categories + ", getKeywordRegexp()=" + getKeywordRegexp()
				+ ", getTypes()=" + getTypes() + "]";
	}

}