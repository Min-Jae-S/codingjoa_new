console.log("## Likes service ready - likes.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let likesService = (function() {

	const contextPath = getContextPath();

	function toggleBoardLikes(boardIdx, callback) {
		console.log("## Toggle Board Likes");
		let url = contextPath + "/api/boards/" + boardIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(JSON.stringify(errorResponse, null, 2));
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert(errorResponse.errorMessage);
				}
			}
		});
	}
	
	function toggleCommentLikes(commentIdx, callback) {
		console.log("## Toggle Comment Likes");
		let url = contextPath + "/api/comments/" + commentIdx + "/likes";
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(JSON.stringify(errorResponse, null, 2));
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert(errorResponse.errorMessage);
				}
			}
		});
	}
	
	return {
		toggleBoardLikes:toggleBoardLikes,
		toggleCommentLikes:toggleCommentLikes
	};
	
})();