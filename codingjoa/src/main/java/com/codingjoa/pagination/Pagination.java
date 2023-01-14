package com.codingjoa.pagination;

import lombok.Data;

@Data
public class Pagination {

	private int startPage;		// 각 페이지에서 시작 번호
	private int endPage;		// 각 페이지에서 끝 번호
	private int prevPage;		// 이전 페이지 번호
	private int nextPage;		// 다음 페이지 번호
	private int page;			// 현재 페이지 번호
	private int pageCnt;		// 전체 페이지 개수
	
	// totalCnt 	: 전체 글 개수
	// page			: 현제 페이지 번호
	// recordCnt 	: 페이지 당 글 개수
	// pageRange	: 페이지 범위, 페이지 버튼 개수
	public Pagination(int totalCnt, int page, int recordCnt, int pageRange) {
		this.page = page;
		this.pageCnt = totalCnt / recordCnt;
		
		// 556/10 = 55 --> 56
		if (totalCnt % recordCnt > 0) {
			pageCnt++;
		}
		
		//	RANGE	|	START	|		END		
		// ------------------------------------------
		// 1  -	10	|	  1		|	1  + 10 - 1 = 10
		// 11 -	20	|	  11	|	11 + 10 - 1 = 20
		// 21 -	30	|	  21	|	21 + 10 - 1 = 30
		startPage = ((page - 1) / recordCnt) * recordCnt + 1;
		
		// endPage - startPage = pageRange - 1
		endPage = startPage + pageRange - 1;
		
		if (endPage > pageCnt) {
			endPage = pageCnt;
		}
		
		prevPage = startPage - 1;
		if (prevPage < 1) {
			prevPage = startPage;
		}
		
		nextPage = endPage + 1;
		if (nextPage > pageCnt) {
			nextPage = pageCnt;
		}
	}
}
