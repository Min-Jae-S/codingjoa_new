console.log("## Rendering service ready - render.js");
	
	function makeCommentHtml(list, boardWriterIdx) {
		let html = "<ul class='list-group list-group-flush mt-4'>";
		
		$.each(list, function(index, commentDetails) {
			if (commentDetails == null) {
			//if (!commentDetails.commentUse) {
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
			
			html += "<li class='list-group-item' comment-idx='" + commentDetails.commentIdx + "'>";
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
			html += "<div class='comment-content' style='white-space: pre-wrap;'>";
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
			html += "<div class='comment-likes'>";
			html += "<button class='btn btn-sm'>";
			html += "<span><i class='fa-regular fa-thumbs-up mr-1'></i>" + commentDetails.commentLikes + "</span>";
			html += "</button>";
			html += "</div>";
			html += "</div>";
			html += "</div>";
			html += "</li>";
		});
		html += "</ul>";
		
		return html;
	}
	
	function makeEditCommentHtml(commentDetails) {
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