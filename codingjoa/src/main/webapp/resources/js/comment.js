function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let commentService = (function() {
	const contextPath = getContextPath();
	
	function writeComment(comment, callback) {
		console.log("## writeComment");
		let url = contextPath + "/api/comments";
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(comment, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
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
					handleCommentError(errorResponse);
				} else {
					alert("## parsing Error");
				}
			}
		});
		
		/*
		return new Promise(function(resolve, reject) {
			$.ajax({
				type : "POST",
				url : url,
				data : JSON.stringify(comment),
				contentType : "application/json; charset=utf-8",
				dataType : "json",
				success : function(result) {
					resolve(result);
				},
				error : function(jqXHR) {
					let errorResponse = JSON.parse(jqXHR.responseText);
					console.table(JSON.stringify(result, null, 2));
					
					if (jqXHR.status == 401) {
						alert(errorResponse.errorMessage)
					} else if (jqXHR.status == 422) {
						$.each(errorResponse.errorMap, function(errorField, errorMessage) {
							alert(errorMessage);
						});
					} else {
						alert("오류가 발생하였습니다");
					}
					
				}
			});
		});
		*/
	}
	
	function getCommentList(commentBoardIdx, page, callback) {
		console.log("## getCommentList");
		let url = contextPath + "/api/boards/" + commentBoardIdx + "/comments?page=" + page;
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
					handleCommentError(errorResponse);
				} else {
					alert("## parsing Error");
				}
			}
		});
	}

	function getModifyComment(commentIdx, callback) {
		console.log("## getModifyComment");
		let url = contextPath + "/api/comments/" + commentIdx;
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
					handleCommentError(errorResponse);
				} else {
					alert("## parsing Error");
				}
			}
		});
	}
	
	function modifyComment(commentIdx, comment, callback) {
		console.log("## modifyComment");
		let url = contextPath + "/api/comments/" + commentIdx;
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(comment, null, 2));
		
		$.ajax({
			type : "PATCH",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
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
					handleCommentError(errorResponse);
				} else {
					alert("## parsing Error");
				}
			}
		});
	}
	
	function deleteComment(commentIdx, callback) {
		console.log("## deleteComment");
		let url = contextPath + "/api/comments/" + commentIdx;
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "DELETE",
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
					handleCommentError(errorResponse);
				} else {
					alert("## parsing Error");
				}
			}
		});
	}
	
	return {
		writeComment:writeComment,
		getCommentList:getCommentList,
		getModifyComment:getModifyComment,
		modifyComment:modifyComment,
		deleteComment:deleteComment
	};
	
})();