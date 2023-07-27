console.log("## Likes service ready - likes.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

function parseError(jqXHR) {
	try {
		let errorResponse = JSON.parse(jqXHR.responseText);
		console.log(JSON.stringify(errorResponse, null, 2));
		return errorResponse;
	} catch(e) {
		alert("Parsing Error");
		return null;
	}
}

function processErrorResponse(errorResponse) {
	let errorMap = errorResponse.errorMap;
	if (errorMap != null) {
		$.each(errorMap, function(errorField, errorMessage) {
			alert(errorMessage);
		});
	} else {
		alert(errorResponse.errorMessage);
	}
}

let likesService = (function() {

	const contextPath = getContextPath();

	function toggleBoardLikes(boardIdx, callback) {
		console.log("## Toggle Board Likes");
		let url = contextPath + "/api/boards/" + boardIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					processErrorResponse(errorResponse);
				} 
			}
		});
	}
	
	function toggleCommentLikes(commentIdx, callback) {
		console.log("## Toggle Comment Likes");
		let url = contextPath + "/api/comments/" + commentIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "POST",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					processErrorResponse(errorResponse);
				} 
			}
		});
	}
	
	function getBoardLikesCnt(boardIdx, callback) {
		console.log("## Get Board Likes Cnt");
		let url = contextPath + "/api/boards/" + boardIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					processErrorResponse(errorResponse);
				} 
			}
		});
	}

	function getCommentLikesCnt(commentIdx, callback) {
		console.log("## Get Comment Likes Cnt");
		let url = contextPath + "/api/comments/" + commentIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("%c> SUCCESS","color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR","color:red");
				let errorResponse = parseError(jqXHR);
				if (errorResponse != null) {
					processErrorResponse(errorResponse);
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