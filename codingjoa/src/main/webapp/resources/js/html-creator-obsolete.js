const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

function createConfigHtml(data) {
	let html = "";
	
	if (data instanceof Array) { // Array.isArray()
		$.each(data, function(index, item) {
			if (typeof item == "string") {
				html += "<p class='card-text'>";
				html += "<i class='fa-solid fa-asterisk mr-2'></i>" + item + "</p>";
			} else {
				$.each(item, function(key, value) {
					html += "<p class='card-text'>";
					html += "<i class='fa-solid fa-asterisk mr-2'></i>" +  key + "</p>";
					$.each(value, function(index, item) {
						html += "<p class='card-text'>";
						html += "<i class='fa-solid fa-caret-right ml-4 mr-2'></i>" + item + "</p>";
					});
				});
			}
		});	
	} else {
		$.each(data, function(key, value) {
			html += "<p class='card-text'>";
			html += "<i class='fa-solid fa-asterisk mr-2'></i>" +  key + "</p>";
			html += "<p class='card-text'>";
			html += "<i class='fa-solid fa-caret-right ml-4 mr-2'></i>" + value + "</p>";
		});
	} 
	
	return html;
}

function createPagedCommentsHtml(pagedComments) {
	console.log("## createPagedCommentsHtml");
	let html = "";
	if (!pagedComments || pagedComments.length == 0) {
		return html;
	}
	
	html += "<ul class='list-group list-group-flush'>";
	$.each(pagedComments, function(index, commentDetails) {
		if (!commentDetails) {
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
		
		html += `<li class='list-group-item' data-id='${commentDetails.id}'>`;
		html += createCommentHtml(commentDetails);
		html += "</li>";
	});
	html += "</ul>";
	return html;
}

function createCommentHtml(commentDetails) {
	let html = "";
	html += "<div class='comment-thum'>";
	if (commentDetails.writerImagePath) {
		html += `<img src='${contextPath}${commentDetails.writerImagePath}'>`;
	} else {
		html += `<img src='${contextPath}/resources/images/img_profile.png'>`;
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += `<span class='comment-writer'>${commentDetails.writerNickname}</span>`;
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += `<span class='comment-createdat'>${commentDetails.createdAt}</span>`;
	html += `<span class='comment-updatedat d-none'>${commentDetails.updatedAt}</span>`;
	// for test
	html += `<span class="text-danger">(${commentDetails.id})</span>`;
	html += "</div>";
	html += "<div class='dropend ml-auto'>"; // dropright --> dropend
	if (commentDetails.isWriter) {
		html += "<button class='comment-utils-btn' data-bs-toggle='dropdown' data-bs-auto-close='outside'>";
	} else {
		html += "<button class='comment-utils-btn' data-bs-toggle='dropdown' data-bs-auto-close='outside' disabled>";
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
	html += "<p>" + commentDetails.content.replace(/(?:\r\n|\r|\n)/g, "<br>") + "</p>";
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-area-footer'>";
	html += "<button type='button' name='commentLikeBtn'>";
	html += "<span class='icon'>";
	if (commentDetails.isLiked) {
		html += "<i class='fa-thumbs-up fa-fw fa-regular text-primary'></i>";
	} else {
		html += "<i class='fa-thumbs-up fa-fw fa-regular'></i>";
	}

	html += "</span>";
	html += `<span class='comment-like-cnt'>${commentDetails.likeCount}</span>`;	
	html += "</button>";
	html += "</div>";
	html += "</div>";
	return html;
}

function createEditCommentHtml(commentDetails) {
	console.log("## createEditCommentHtml");
	console.log(commentDetails);
	
	let html = "";
	if (!commentDetails.isWriter) {
		return html;
	}
	
	html += "<div class='comment-thum'>";
	if (commentDetails.writerImagePath) {
		html += `<img src='${contextPath}${commentDetails.writerImagePath}'>`;
	} else {
		html += `<img src='${contextPath}/resources/images/img_profile.png'>`;
	}
	
	html += "</div>";
	html += "<div class='comment-area'>";
	html += "<div class='comment-area-header'>";
	html += "<div class='comment-info'>";
	html += `<span class='comment-writer'>${commentDetails.writerNickname}</span>`;
	if (commentDetails.isBoardWriter) {
		html += "<span class='badge badge-pill badge-primary'>글쓴이</span>";
	}
	
	html += `<span class='comment-createdat'>${commentDetails.createdAt}</span>`;
	html += `<span class='comment-updatedat d-none'>${commentDetails.updatedAt}</span>`;
	html += "</div>";
	html += "</div>";
	html += "<div class='comment-edit-wrap'>";
	html += "<form>"
	html += "<div class='input-group'>";
	html += "<div class='comment-edit form-control'>";
	html += `<textarea name='content' rows='1'>${commentDetails.content}</textarea>`;
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

