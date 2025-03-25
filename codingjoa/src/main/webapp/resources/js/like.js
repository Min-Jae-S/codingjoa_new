let likeService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function toggleBoardLike(boardId, callback) {
		console.log("## toggleBoardLike");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/boards/${boardId}/likes`,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function toggleCommentLike(commentId, callback) {
		console.log("## toggleCommentLike");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/comments/${commentId}/likes`,
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
				handleError(parseError(jqXHR)); 
			}
		});
	}
	
	function getBoardLikeCnt(boardId, callback) {
		console.log("## getBoardLikeCnt");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/boards/${boardId}/likes`,
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
				handleError(parseError(jqXHR));
			}
		});
	}

	function getCommentLikeCnt(commentId, callback) {
		console.log("## getCommentLikeCnt");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/comments/${commentId}/likes`,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	return {
		toggleBoardLike:toggleBoardLike,
		toggleCommentLike:toggleCommentLike,
		getBoardLikeCnt:getBoardLikeCnt,
		getCommentLikeCnt:getCommentLikeCnt
	};
	
})();