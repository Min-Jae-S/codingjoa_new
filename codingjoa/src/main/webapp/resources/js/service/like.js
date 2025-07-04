let likeService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function toggleBoardLike(boardId, callback) {
		console.log("## toggleBoardLike");
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/boards/${boardId}/likes`,
			dataType : "json",
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
		toggleCommentLike:toggleCommentLike
	};
	
})();