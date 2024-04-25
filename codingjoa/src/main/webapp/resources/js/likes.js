console.log("## Likes service ready - likes.js");

let likesService = (function() {
	const contextPath = getContextPath();

	function toggleBoardLikes(boardIdx, callback) {
		console.log("## Toggle boardLikes");
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
		console.log("## Toggle commentLikes");
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
		console.log("## Get boardLikesCnt");
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
		console.log("## Get commentLikesCnt");
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