const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

function createCategoryMenuHtml(categoryList) {
	console.log("## createCategoryMenuHtml");
	if (!categoryList || categoryList.length == 0) {
		return "";
	}
	
	return categoryList.map(({ categoryCode, categoryPath, categoryName }) => {
		let path = (categoryCode == categoryPath) ? `/?boardCategoryCode=${categoryCode}` : categoryPath;
		return `<button class='dropdown-item' type='button' data-path='${path}'>${categoryName}</button>`;
	}).join("");
}

function createPagedCommentHtml(pagedComment) {
	console.log("## createPagedCommentHtml");
	let html = "";
	if (!pagedComment || pagedComment.length == 0) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(pagedComment, function(index, commentDetails) {
		if (commentDetails == "") {
			html += "<li class='list-group-item deleted-comment'>";
			html += "<div class='comment-area'>";
			html += "<div class='comment-area-header'>";
			html += "<div class='comment-info'>";
			html += "<span class='comment-writer'>삭제된 댓글</span>";
			html += "</div>";
			html += "</div>";
			html += "<div class='comment-area-body'>";
			html += "<div class='comment-content' style='line-height:180%;'>";
			html += "<p>삭제된 댓글입니다.</p>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</li>";
			return true;
		}
		
		html += "<li class='list-group-item' data-idx='" + commentDetails.commentIdx + "'>";
		html += createCommentHtml(commentDetails);
		html += "</li>";
	});
	html += "</ul>";
	return html;
}

function createCommentHtml(commentDetails) {
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.commentWriterImageUrl == "") {
		//html += "<img src='/codingjoa/resources/images/img_profile.png'>";
		html += "<img src='../resources/images/img_profile.png'>";
	} else {
		html += "<img src='" + commentDetails.commentWriterImageUrl + "'>";
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += "<span class='comment-writer'>" + commentDetails.commentWriterNickname + "</span>";
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += "<span class='comment-createdat'>" + commentDetails.createdAt + "</span>";
	html += "<span class='comment-updatedat d-none'>" + commentDetails.updatedAt + "</span>";
	html += "</div>";
	html += "<div class='dropright ml-auto'>";
	if (commentDetails.isCommentWriter) {
		html += "<button class='comment-utils-btn' data-toggle='dropdown'>";
	} else {
		html += "<button class='comment-utils-btn' data-toggle='dropdown' disabled>";
	}
	
	html += "<i class='fa-ellipsis-vertical fa-solid'></i>";
	html += "</button>";
	html += "<ul class='dropdown-menu'>";
	html += "<h6 class='dropdown-header'>댓글 관리</h6>";
	html += "<hr class='dropdown-divider'>";
	html += "<li>";
	html += "<button class='dropdown-item' type='button' name='showEditCommentBtn'>수정하기</button>";
	html += "<button class='dropdown-item' type='button' name='deleteCommentBtn'>삭제하기</button>";
	html += "</li>";
	html += "</ul>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-body'>";
	html += "<div class='comment-content'>";
	html += "<p>" + commentDetails.commentContent.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-footer'>";
	html += "<button type='button' name='commentLikesBtn'>";
	html += "<span class='icon'>";
	if (commentDetails.isCommentLiked) {
		html += "<i class='fa-thumbs-up fa-fw fa-regular text-primary'></i>";
	} else {
		html += "<i class='fa-thumbs-up fa-fw fa-regular'></i>";
	}

	html += "</span>";
	html += "<span class='comment-likes-cnt'>" + commentDetails.commentLikesCnt + "</span>";	
	html += "</button>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.commentWriterImageUrl == "") {
		//html += "<img src='/codingjoa/resources/images/img_profile.png'>";
		html += "<img src='../resources/images/img_profile.png'>";
	} else {
		html += "<img src='" + commentDetails.commentWriterImageUrl + "'>";
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += "<span class='comment-writer'>" + commentDetails.commentWriterNickname + "</span>";
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += "<span class='comment-createdat'>" + commentDetails.createdAt + "</span>";
	html += "<span class='comment-updatedat d-none'>" + commentDetails.updatedAt + "</span>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-edit-wrap'>";
	html += "<form>"
	html += "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += "<textarea name='commentContent' rows='1'>" + commentDetails.commentContent + "</textarea>";
	html += "<div class='mt-2'>";
	html += "<button type='submit' class='btn btn-sm btn-outline-primary'>수정</button>";
	html += "<button type='button' class='btn btn-sm btn-outline-secondary ml-2'>취소</button>";
	html += "</div>";		
	html += "</div>";			
	html += "</div>";	
	html += "</form>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createPaginationHtml(pagination) {
	if (!pagination) {
		return "";
	}
	
	const { startPage, endPage, prevPage, nextPage, page, pageCnt, prev, next, first, last } = pagination;
	
	
	let firstPageBtn = "";
	if (first) {
		firstPageBtn = `
			<li class="page-item">
				<button type='button' class='page-link' data-page='1'>
					<i class='fa-solid fa-fw fa-angles-left'></i>
				</button>
			</li>`;
	}
	
	let prevPageBtn = "";
	if (prev) {
		prevPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${prevPage}'>
					<i class='fa-solid fa-fw fa-angle-left'></i>
				</button>
			</li>`;
	}
	
	let pageBtns = "";
	for (let i = startPage; i <= endPage; i++) {
		pageBtns += `
			<li class='page-item ${i == page ? "active" : ""}'>
				<button type='button' class='page-link' data-page='${i}'>${i}</button>
			</li>`;
	}
	
	let nextPageBtn = "";
	if (next) {
		nextPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${nextPage}'>
					<i class='fa-solid fa-fw fa-angle-right'></i>
				</button>
			</li>`;
	}

	let lastPageBtn = "";
	if (last) {
		lastPageBtn = `
			<li class='page-item'>
				<button type='button' class='page-link' data-page='${pageCnt}'>
					<i class='fa-solid fa-fw fa-angles-right'></i>
				</button>
			</li>`;
	}

	return `
		<ul class='pagination'>
			${firstPageBtn}
			${prevPageBtn}
			${pageBtns}
			${nextPageBtn}
			${lastPageBtn}
		</ul>`;
}

// =============================================
//				ADMIN HTML CREATOR
// =============================================

function createErrorPage() {
	return `
		<div class="error-wrap">
			<p class="error-code">404</p>
			<p class="error-message">요청한 페이지를 찾을 수 없습니다.</p>
		</div>`;
}

function createWelcomePage() {
	return `<p class='welcome'>Welcome to Admin Dashboard</p>`;
}

function createBoardsPageHtml(result) {
	console.log("## createBoardsPageHtml");
	const { options, adminBoardCri, pagedBoards, pagination } = result.data;
	
	let formHtml = createBoardsFormHtml(options, adminBoardCri);
	let tableHtml = createBoardsTableHtml(pagedBoards);
	let paginationHtml = createPaginationHtml(pagination);
	
	return `
		<div class="card rounded-xl">
			<div class="card-body">
				<div class="form-wrap">
					${formHtml}
				</div>
				<div class="table-wrap">
					${tableHtml}
				</div>
				<div class="board-pagination">
					${paginationHtml}
				</div>
			</div>
		</div>`;
}

function createBoardsFormHtml(options, adminBoardCri) {
	let typeOptionHtml = Object.entries(options.typeOption)
		.map(([key, value]) => 
			`<option value="${key}" ${key == adminBoardCri.type ? "selected" : ""}>${value}</option>`
		)
		.join("");
	
	let recordCntOptionHtml = Object.entries(options.recordCntOption)
		.map(([key, value]) => 
			`<option value="${key}" ${key == adminBoardCri.recordCnt ? "selected" : ""}>${value}</option>`
		)
		.join("");
	
	let categoryOptionHtml = Object.entries(options.categoryOption)
		.map(([key, value]) => 
			`<option value="${key}">${value}</option>`
		)
		.join("");
	
	return `
		<form id="adminBoardsForm">
			<div class="d-flex mb-3">
				<select id="type" name="type" class="custom-select mr-4 rounded-md w-20">
					${typeOptionHtml}
				</select>
				<div class="input-group">
					<input id="keyword" name="keyword" class="form-control rounded-md" value="${adminBoardCri.keyword}" 
						placeholder="검색어를 입력해주세요"/>
					<div class="input-group-append">
						<button type="submit" class="btn btn-outline-secondary rounded-md">검색</button>
					</div>
				</div>
			</div>
			<div>
				<select id="recordCnt" name="recordCnt" class="custom-select rounded-md w-20 ml-auto">
					${recordCntOptionHtml}
				</select>
			</div>
			<div>
				<select id="categories" name="categories" class="custom-select rounded-md w-20 ml-auto">
					${categoryOptionHtml}
				</select>
			</div>
		</form>`;
	
//	return `
//		<form id="adminBoardsForm" class="form-inline">
//			<button type="button" id="deleteBoardsBtn" class="btn btn-outline-secondary rounded-md mr-auto" disabled>선택삭제</button>
//			<div class="d-flex">
//				<select id="type" name="type" class="custom-select mr-3 rounded-md">
//					${typeOptionHtml}
//				</select>
//				<div class="input-group mr-3">
//					<input id="keyword" name="keyword" class="form-control rounded-md" value="${adminBoardCri.keyword}" 
//						placeholder="검색어를 입력해주세요"/>
//					<div class="input-group-append">
//						<button type="submit" class="btn btn-outline-secondary rounded-md">검색</button>
//					</div>
//				</div>
//				<select id="recordCnt" name="recordCnt" class="custom-select rounded-md ml-auto">
//					${recordCntOptionHtml}
//				</select>
//			</div>
//		</form>`;

}

function createBoardsTableHtml(pagedBoards) {
	let rowsHtml = "";
	if (!pagedBoards || pagedBoards.length == 0) {
		rowsHtml = `
			<tr>
				<td colspan="9">
					<div class="no-board">등록된 게시글이 없습니다.</div>
				</td>	
			</tr>`;
	} else {
		rowsHtml = pagedBoards.map(adminBoard => `
			<tr>
				<td class="d-md-table-cell">
					<div class="form-check">
						<input class="form-check-input position-static" type="checkbox" name="boardIds" value="${adminBoard.boardIdx}">
					</div>
				</td>		
				<td class="d-md-table-cell">
					<span>${adminBoard.boardIdx}</span>
				</td>
				<td class="d-md-table-cell text-left">
					<a href="${contextPath}/board/read?boardIdx=${adminBoard.boardIdx}">${adminBoard.boardTitle}</a>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.writerNickname}</span></br>
					<span class="email">${adminBoard.writerEmail}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.categoryName}</span>
				</td>
				<td class="d-md-table-cell">
					<span class="created-at">${adminBoard.createdAt}</span></br>
					${adminBoard.isUpdated ? `<span class="updated-at">${adminBoard.updatedAt}</span>` : ``}
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.boardViews}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.likesCnt}</span>
				</td>
				<td class="d-md-table-cell">
					<span>${adminBoard.commentCnt}</span>
				</td>
			</tr>`).join("");
	}
	
	return `
		<table class="table">
			<thead>
				<tr>
					<th class="d-md-table-cell">
						<div class="form-check">
					  		<input class="form-check-input position-static" type="checkbox" id="toggleAllBoards">
						</div>
					</th>
					<th class="d-md-table-cell">번호</th>
					<th class="d-md-table-cell">제목</th>
					<th class="d-md-table-cell">작성자</th>
					<th class="d-md-table-cell">게시판</th>
					<th class="d-md-table-cell">작성일 (수정일)</th>
					<th class="d-md-table-cell">조회</th>
					<th class="d-md-table-cell">좋아요</th>
					<th class="d-md-table-cell">댓글</th>
				</tr>
			</thead>
			<tbody>
				${rowsHtml}
			</tbody>
		</table>`;
}


