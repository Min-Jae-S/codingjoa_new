console.log("## renderService ready - render.js");

function createCommentHtml(commentList, myCommentLikes, boardWriterIdx) {
	console.log("## createCommentHtml");
	let html = "";
	if (!(commentList.length > 0)) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(commentList, function(index, commentDetails) {
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
		//html += "<span class='comment-writer'>" + commentDetails.commentWriterId + "</span>";
		html += "<span class='comment-writer'>" + commentDetails.commentWriterId + " (commentIdx = " + commentDetails.commentIdx + ")</span>";
		if (commentDetails.commentWriterIdx == boardWriterIdx) {
			html += "<span class='badge badge-pill badge-primary mr-1'>작성자</span>"
		}
		html += "<span class='comment-regdate'>" + commentDetails.regdate + "</span>";
		html += "<span class='comment-moddate d-none'>" + commentDetails.moddate + "</span>";
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
		if (myCommentLikes.includes(commentDetails.commentIdx)) {
			html += "<i class='text-primary fa-regular fa-thumbs-up'></i> ";
		} else {
			html += "<i class='text-grey fa-regular fa-thumbs-up'></i> ";
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

function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	let html = "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += "<p class='font-weight-bold mb-2'>" + commentDetails.commentWriterId + "</p>";
	html += "<textarea rows='1' style='white-space: pre;'>" + commentDetails.commentContent + "</textarea>";
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
	if (!(pagination.totalCnt > 0)) {
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
