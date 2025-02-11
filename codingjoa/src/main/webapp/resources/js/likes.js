let likesService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));

	function toggleBoardLikes(boardIdx, callback) {
		console.log("## toggleBoardLikes");
		let url = contextPath + "/api/boards/" + boardIdx + "/likes";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleLikesError(errorResponse);
				} else {
					alert("## Parsing Error");
				} 
			}
		});
	}
	
	function toggleCommentLikes(commentIdx, callback) {
		console.log("## toggleCommentLikes");
		let url = contextPath + "/api/comments/" + commentIdx + "/likes";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleLikesError(errorResponse);
				} else {
					alert("## Parsing Error");
				} 
			}
		});
	}
	
	function getBoardLikesCnt(boardIdx, callback) {
		console.log("## getBoardLikesCnt");
		let url = contextPath + "/api/boards/" + boardIdx + "/likes";
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleLikesError(errorResponse);
				} else {
					alert("## Parsing Error");
				} 
			}
		});
	}

	function getCommentLikesCnt(commentIdx, callback) {
		console.log("## getCommentLikesCnt");
		let url = contextPath + "/api/comments/" + commentIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					handleLikesError(errorResponse);
				} else {
					alert("## Parsing Error");
				} 
			}
		});
	}
	
	return {
		toggleBoardLikes:toggleBoardLikes,
		toggleCommentLikes:toggleCommentLikes,
		getBoardLikesCnt:getBoardLikesCnt,
		getCommentLikesCnt:getCommentLikesCnt
	};
	
})();