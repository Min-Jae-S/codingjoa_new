package com.codingjoa.pagination;

import lombok.Data;

@Data
public class Pagination {

	private int minPage;		// 최소 페이지 번호
	private int maxPage;		// 최대 페이지 번호
	private int prevPage;		// 이전 페이지 번호
	private int nextPage;		// 다음 페이지 번호
	private int page;			// 현재 페이지 번호
	private int pageCnt;		// 전체 페이지 개수
	
	// listCnt 		: 전체 글 개수
	// page			: 현제 페이지 번호
	// recordSize 	: 페이지 당 글 개수
	// pageRange	: 페이지 범위, 페이지 버튼 개수
	public Pagination(int listCnt, int page, int recordSize, int pageRange) {
		this.page = page;
		this.pageCnt = listCnt / recordSize;
		
		// 556/10 = 55 --> 56
		if (listCnt % recordSize > 0) {
			pageCnt++;
		}
		
		
	}
}
