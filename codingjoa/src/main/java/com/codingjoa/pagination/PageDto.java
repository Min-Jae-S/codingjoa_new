package com.codingjoa.pagination;

import lombok.Data;

@Data
public class PageDto {

	private int startPage;			// 각 페이지 범위에서의 시작 번호
	private int endPage;			// 각 페이지 범위에서의 끝 번호
	private int pageSize;			// 보이는 페이지 개수, 페이지 범위
	private int total;				// 전체 레코드 개수
	private boolean prev, next; 	// 이전, 다음페이지 여부
	
	private Criteria cri;			// 현재 페이지(page), 페이지 당 레코드 개수(recordSize)
									// 검색 유형(type), 검색어(keyword)
	
	public PageDto(int total, Criteria cri) {
		this.total = total;
		this.cri = cri;
		this.pageSize = 10;
		
		
	}
}
