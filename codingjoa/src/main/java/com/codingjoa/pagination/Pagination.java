package com.codingjoa.pagination;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class Pagination {

	private int startPage;		// 각 페이지에서 시작 번호
	private int endPage;		// 각 페이지에서 끝 번호
	private int prevPage;		// 이전 페이지 번호
	private int nextPage;		// 다음 페이지 번호
	private int page;			// 현재 페이지 번호
	private int pageCnt;		// 전체 페이지 개수
	private int totalCnt;		// 전체 레코드 개수
	private boolean prev; 		// 이전 페이지 이동 가능 여부
	private boolean next;		// 다음 페이지 이동 가능 여부
	private boolean first;		// 처음 페이지 이동 가능 여부
	private boolean last;		// 마지막 페이지 이동 가능 여부
	
	/*
	 * totalCnt 	: 전체 레코드 개수
	 * page			: 현제 페이지 번호
	 * recordCnt 	: 페이지 당 레코드 개수
	 * pageRange	: 페이지 범위 (페이지 버튼 개수)
	 */
	
	public Pagination(int totalCnt, int page, int recordCnt, int pageRange) {
		this.totalCnt = totalCnt;
		
		// 556/10 = 55 --> 56
		pageCnt = totalCnt / recordCnt;
		if (totalCnt % recordCnt > 0) {
			pageCnt += 1; // pageCnt++
		}
		
		if (page < 1) {
			page = 1;
		}
		
		if (page > pageCnt) {
			page = pageCnt;
		}
		
		this.page = page;
		
		// | 	 RANGE		|	START	|	 END	|
		// ------------------------------------------
		// |  	1  - 10		|	  1		|	 10		|
		// |  	11 - 20		|	  11	|	 20		|
		// |  	21 - 30		|	  21	|	 30		|
		
		startPage = ((page - 1) / pageRange) * pageRange + 1;
		
		endPage = startPage + pageRange - 1;
		if (endPage > pageCnt) {
			endPage = pageCnt;
		}
		
		prevPage = startPage -1;
		if (prevPage < 1) {
			prevPage = startPage;
			prev = false;
		} else {
			prev = true;
		}
		
		nextPage = endPage + 1;
		if (nextPage > pageCnt) {
			nextPage = pageCnt;
			next = false;
		} else {
			next = true;
		}
		
		first = startPage > 1;
		last = endPage < pageCnt;
	}
}
