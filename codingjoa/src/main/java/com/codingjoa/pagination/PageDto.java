package com.codingjoa.pagination;

import lombok.Data;

@Data
public class PageDto {

	private int page;			// 현재 페이지
	private int startPage;		// 각 페이지 범위에서의 시작 번호
	private int endPage;		// 각 페이지 범위에서의 끝 번호
	private int recordSize;		// 페이지 당 보이는 리스트의 개수		
	private int pageSize;		// 보이는 페이지의 개수
	
	private boolean prev, next; // 이전, 다음페이지 여부
}
