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

function createCommentHtml(pagedComment) {
	console.log("## createCommentHtml");
	let html = "";
	if (pagedComment.length == 0) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(pagedComment, function(index, commentDetails) {
		if (commentDetails == null) {
			html += "<li class='list-group-item deleted-comment'>";
			html += "<div class='comment-area'>";
			html += "<div class='comment-area-header'>";
			html += "<div class='comment-info'>";
			html += "<span class='comment-writer'>삭제된 댓글</span>";
			html += "</div>";
			html += "<div class='comment-content'>";
			html += "<span>삭제된 댓글입니다.</span>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</li>";
			return true;
		}
		
		html += "<li class='list-group-item' data-comment-idx='" + commentDetails.commentIdx + "'>";
		html += "<div class='comment-area'>";
		html += "<div class='comment-area-header'>";
		html += "<div class='comment-info'>";
		html += "<span class='comment-writer mr-2'>" + commentDetails.memberNickname + "</span>";
		if (commentDetails.isBoardWriter) {
			html += "<span class='badge badge-pill badge-primary mr-2'>작성자</span>"
		}
		html += "<span class='comment-regdate'>" + commentDetails.createdAt + "</span>";
		html += "<span class='comment-moddate d-none'>" + commentDetails.updatedAt + "</span>";
		html += "</div>";
		html += "<div class='comment-content' style='white-space:pre-wrap;line-height:180%;'>";
		html += "<p>" + commentDetails.commentContent.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
		html += "</div>";
		html += "</div>";
		html += "<div class='comment-area-footer'>";
		html += "<div class='dropright'>";
		html += "<button class='comment-utils btn' data-toggle='dropdown' data-offset='0,10'>";
		html += "<i class='fa-solid fa-ellipsis-vertical'></i>";
		html += "</button>";
		html += "<div class='dropdown-menu'>";
		html += "<h6 class='dropdown-header'>댓글 관리</h6>";
		html += "<button class='dropdown-item' name='showEditCommentBtn'>수정하기</button>";
		html += "<button class='dropdown-item' name='deleteCommentBtn'>삭제하기</button>";
		html += "</div>";
		html += "</div>";
		html += "<div class='mt-auto'>"
		html += "<button class='btn border-0 p-0 shadow-none' type='button' name='commentLikesBtn'>";
		if (commentDetails.isCommentLiked) {
			html += "<i class='fa-thumbs-up fa-regular text-primary'></i> ";
		} else {
			html += "<i class='fa-thumbs-up fa-regular text-grey'></i>";
		}
		html += "<span class='comment-likes-cnt'>" + commentDetails.commentLikesCnt + "</span>";
		html += "</button>";
		html += "</div>";
		html += "</div>";
		html += "</div>";
		html += "</li>";
	});
	html += "</ul>";
	return html;
}

function createEditCommentHtml(commentContent) {
	console.log("## createEditCommentHtml");
	let html = "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	//html += "<p class='font-weight-bold mb-2'>" + commentDetails.memberNickname + "</p>";
	html += "<textarea rows='1' style='white-space: pre;'>" + commentContent + "</textarea>";
	html += "<div class='mt-2'>";
	html += "<button class='btn btn-outline-primary btn-sm mr-2' name='modifyCommentBtn'>수정</button>";
	html += "<button class='btn btn-outline-secondary btn-sm' name='closeEditCommentBtn'>취소</button>";
	html += "</div>";		
	html += "</div>";			
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
