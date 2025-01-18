function createCategoryMenuHtml(categoryList) {
	console.log("## createCategoryMenuHtml");
	let html = "";
	if (categoryList.length == 0) {
		return html;
	}
	
	$.each(categoryList, function(i, value) {
		let categoryCode = categoryList[i].categoryCode;
		let categoryPath = categoryList[i].categoryPath;
		let categoryName = categoryList[i].categoryName;
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
	$.each(pagedComment, function(i, commentDetails) {
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
		html += "<img src='/codingjoa/resources/images/img_profile.png'>";
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
	//html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' ${commentDetails.isCommentWriter ? '' : 'disabled'}>";
	if (commentDetails.isCommentWriter) {
		html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' aria-expanded='false'>";
	} else {
		html += "<button class='comment-utils-btn btn' data-toggle='dropdown' data-offset='0,10' aria-expanded='false' disabled>";
	}
	
	html += "<i class='fa-ellipsis-vertical fa-solid'></i>";
	html += "</button>";
	html += "<div class='dropdown-menu'>";
	html += "<h6 class='dropdown-header'>댓글 관리</h6>";
	html += "<button class='dropdown-item' type='button' name='showEditCommentBtn'>수정하기</button>";
	html += "<button class='dropdown-item' type='button' name='deleteCommentBtn'>삭제하기</button>";
	html += "</div>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-body'>";
	html += "<div class='comment-content'>";
	html += "<p>" + commentDetails.commentContent.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-footer'>";
	if (commentDetails.isCommentLiked) {
		html += "<button type='button' name='commentLikesBtn' class='on'>";
	} else {
		html += "<button type='button' name='commentLikesBtn'>";
	}

	html += "<i class='fa-thumbs-up fa-fw fa-regular'></i>";
	html += " <span class='comment-likes-cnt'>" + commentDetails.commentLikesCnt + "</span>"; // 앞의 공백을 의도적으로 추가		
	html += "</button>";
	html += "</div>";
	html += "</div>";
	return html;
}

/*function createEditCommentHtml(commentDetails) {
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.commentWriterImageUrl == "") {
		html += "<img src='/codingjoa/resources/images/img_profile.png'>";
	} else {
		html += "<img src='" + commentDetails.commentWriterImageUrl + "'>";
	}
	
	html += "</div>";
	html += "<div class='comment-edit-wrap'>";
	html += "<form>"
	html += "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += "<p class='font-weight-bold mb-2'>" + commentDetails.commentWriterNickname + "</p>";
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
}*/

function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.commentWriterImageUrl == "") {
		html += "<img src='/codingjoa/resources/images/img_profile.png'>";
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
	console.log("## createPaginationHtml");
	let html = "";
	if (pagination == "") {
		return html;
	}
	
	html += "<ul class='pagination my-0'>";
	if (pagination.prev) {
		html += "<li class='page-item'>";
		html += "<a class='page-link' href='#' data-page='" + pagination.prevPage + "'>";
		html += "<i class='fa-chevron-left fa-solid'></i>";
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
		html += "<i class='fa-chevron-right fa-solid'></i>";
		html += "</a>";
		html += "</li>";
	}
	html += "</ul>";
	return html;
}
