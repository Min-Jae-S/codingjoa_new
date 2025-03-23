let commentService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));;
	
	function write(comment, callback) {
		console.log("## write");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/comments`,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.dir(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleCommentError(errorResponse);
				} else {
					alert("## parsing error");
				}
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
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleCommentError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}

	function modify(commentId, comment, callback) {
		console.log("## modify");
		$.ajax({
			type : "PATCH",
			url : `${contextPath}/api/comments/${commentId}`,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleCommentError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	function delete(commentId, callback) {
		console.log("## delete");
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/comments/${commentId}`,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleCommentError(errorResponse);
				} else {
					alert("## parsing error");
				}
			}
		});
	}
	
	return {
		write:write,
		getPagedComments:getPagedComments,
		modify:modify,
		delete:delete
	};
	
})();