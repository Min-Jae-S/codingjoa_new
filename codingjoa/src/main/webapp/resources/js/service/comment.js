let commentService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));;
	
	function writeComment(comment, callback) {
		console.log("## writeComment");
		console.log(JSON.stringify(comment, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/comments`,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.dir(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function getPagedComments(boardId, page, callback) {
		console.log("## getPagedComments");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/boards/${boardId}/comments?page=${page}`,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}

	function modifyComment(commentId, comment, callback) {
		console.log("## modifyComment");
		console.log(JSON.stringify(comment, null, 2));
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/comments/${commentId}`,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function deleteComment(commentId, callback) {
		console.log("## deleteComment");
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/comments/${commentId}`,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	return {
		writeComment:writeComment,
		getPagedComments:getPagedComments,
		modifyComment:modifyComment,
		deleteComment:deleteComment
	};
	
})();