function createCategoryMenuHtml(categoryList) {
	console.log("## createCategoryMenuHtml");
	if (!categoryList || categoryList.length == 0) {
		return "";
	}
	
	return categoryList.map(category => {
		let path = (category.categoryCode == category.categoryPath) ? 
				`/?boardCategoryCode=${category.categoryCode}` : category.categoryPath;
		return `<button class='dropdown-item' type='button' data-path='${path}'>${category.categoryName}</button>`;
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
