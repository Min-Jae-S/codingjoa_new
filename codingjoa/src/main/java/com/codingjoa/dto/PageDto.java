package com.codingjoa.dto;

import lombok.Data;

@Data
public class PageDto {

	private int curPage;			// 현재 페이지
	private int startPage;			// 각 페이지 범위에서의 시작 번호
	private int endPage;			// 각 페이지 범위에서의 끝 번호
	private int recordsPerPage;		// 페이지 당 보여질 리스트의 개수
	
	private boolean prev, next; // 이전, 다음페이지 여부
}
