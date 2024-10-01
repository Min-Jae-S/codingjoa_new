function createCategoryMenuHtml(categoryList) {
	console.log("## createCategoryMenuHtml");
	let html = "";
	if (categoryList.length == 0) {
		return html;
	}
	
	$.each(categoryList, function(index, value) {
		let categoryCode = categoryList[index].categoryCode;
		let categoryPath = categoryList[index].categoryPath;
		let categoryName = categoryList[index].categoryName;
		let path = (categoryCode == categoryPath) ? "/?boardCategoryCode=" + categoryCode : categoryPath;
		html += "<button class='dropdown-item' type='button' data-path='" + path + "'>" + categoryName + "</button>";
	});
	
	return html;
}

function createPagedCommentHtml(pagedComment) {
	console.log("## createPagedCommentHtml");
	let html = "";
	if (pagedComment.length == 0) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(pagedComment, function(index, commentDetails) {
		if (commentDetails == "") {
			html += "<li class='list-group-item'>";
			html += "<div class='comment-area'>";
			html += "<div class='comment-area-header'>";
			html += "<div class='comment-info'>";
			html += "<span class='comment-writer'>삭제된 댓글</span>";
			html += "</div>";
			html += "</div>";
			html += "<div class='comment-area-body'>";
			html += "<div class='comment-content deleted-comment' style='line-height:180%;'>";
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
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += "<span class='comment-writer mr-2'>" + commentDetails.memberNickname + "</span>";
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary mr-2'>작성자</span>"
	}
	html += "<span class='comment-createdat'>" + commentDetails.createdAt + "</span>";
	html += "<span class='comment-updatedat d-none'>" + commentDetails.updatedAt + "</span>";
	html += "</div>";
	html += "<div class='dropright ml-auto'>";
	//html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' ${commentDetails.isCommentWriter ? '' : 'disabled'}>";
	if (commentDetails.isCommentWriter) {
		html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' aria-expanded='false'>";
	} else {
		html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' aria-expanded='false' disabled>";
	}
	
	html += "<i class='fa-solid fa-ellipsis-vertical'></i>";
	html += "</button>";
	html += "<div class='dropdown-menu'>";
	html += "<h6 class='dropdown-header'>댓글 관리</h6>";
	html += "<button class='dropdown-item' type='button' name='showEditCommentBtn'>수정하기</button>";
	html += "<button class='dropdown-item' type='button' name='deleteCommentBtn'>삭제하기</button>";
	html += "</div>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-body'>";
	//html += "<div class='comment-content' style='white-space:pre-wrap;line-height:180%;'>";
	html += "<div class='comment-content' style='line-height:180%;'>";
	html += "<p>" + commentDetails.commentContent.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-footer'>";
	html += "<button class='btn border-0 p-0 shadow-none ml-auto' type='button' name='commentLikesBtn'>";
	if (commentDetails.isCommentLiked) {
		html += "<i class='fa-thumbs-up fa-regular text-primary'></i>";
	} else {
		html += "<i class='fa-thumbs-up fa-regular text-grey'></i>";
	}
	html += " <span class='comment-likes-cnt'>" + commentDetails.commentLikesCnt + "</span>"; // 앞의 공백을 의도적으로 추가		
	html += "</button>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createEditCommentHtml(commentDetails) {
	let html = "";
	html += "<div class='comment-edit-wrap'>";
	html += "<form>"
	html += "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += "<p class='font-weight-bold mb-2'>" + commentDetails.memberNickname + "</p>";
	//html += "<textarea rows='1' style='white-space:pre;style='line-height:180%;'>" + commentDetails.commentContent + "</textarea>";
	html += "<textarea name='commentContent' rows='1' style='line-height:180%;'>" + commentDetails.commentContent + "</textarea>";
	html += "<div class='mt-2'>";
	html += "<button class='btn btn-sm btn-outline-primary' type='submit'>수정</button>";
	html += "<button class='btn btn-sm btn-outline-secondary ml-2' type='button'>취소</button>";
	html += "</div>";		
	html += "</div>";			
	html += "</div>";	
	html += "</form>";
	html += "</div>";
	return html;
}

function createPaginationHtml(pagination) {
	console.log("## createPaginationHtml");
	let html = "";
	if (pagination == "") {
		return html;
	}
	
	html += "<ul class='pagination my-0'>";
	if (pagination.prev) {
		html += "<li class='page-item'>";
		html += "<a class='page-link' href='#' data-page='" + pagination.prevPage + "'>";
		html += "<i class='fa-solid fa-chevron-left'></i>";
		html += "</a>";
		html += "</li>";
	}
	
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		if (i == pagination.page) {
			html += "<li class='page-item active'>";
		} else {
			html += "<li class='page-item'>";
		}
		html += "<a class='page-link' href='#' data-page='" + i + "'>" + i + "</a>";
		html += "</li>";
	}
	
	if (pagination.next) {
		html += "<li class='page-item'>";
		html += "<a class='page-link' href='#' data-page='" + pagination.nextPage + "'>";
		html += "<i class='fa-solid fa-chevron-right'></i>";
		html += "</a>";
		html += "</li>";
	}
	html += "</ul>";
	return html;
}
