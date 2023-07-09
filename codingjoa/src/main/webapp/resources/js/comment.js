console.log("## Comment service ready - comment.js");

function getContextPath() {
    let hostIndex = location.href.indexOf(location.host) + location.host.length;
    return location.href.substring(hostIndex, location.href.indexOf('/', hostIndex + 1));
}

let commentService = (function() {

	const contextPath = getContextPath();
	
	function writeComment(comment, callback) {
		console.log("## Write Comment");
		let url = contextPath + "/api/comments";
		console.log("> url = '%s'", url);
		console.log("> comment = %s", JSON.stringify(comment, null, 2));
		
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("## success = %s", JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## error = %s", JSON.stringify(errorResponse, null, 2));
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert(errorResponse.errorMessage);
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
		console.log("## Get Comment List");
		let url = contextPath + "/api/boards/" + commentBoardIdx + "/comments?page=" + page;
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
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

	function getComment(commentIdx, callback) {
		console.log("## Get Comment");
		let url = contextPath + "/api/comments/" + commentIdx;
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
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
	
	function modifyComment(commentIdx, comment, callback) {
		console.log("## Modify Comment");
		let url = contextPath + "/api/comments/" + commentIdx;
		console.log("> url = '%s'", url);
		console.log("> comment = %s", JSON.stringify(comment, null, 2));
		
		$.ajax({
			type : "PATCH",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert(errorResponse.errorMessage)
				}
			}
		});
	}
	
	function deleteComment(commentIdx, callback) {
		console.log("## Delete Comment");
		let url = contextPath + "/api/comments/" + commentIdx;
		console.log("> url = '%s'", url);
		
		$.ajax({
			type : "DELETE",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log("## Success Response");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log("## Error Response");
				console.log(JSON.stringify(errorResponse, null, 2));
				if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert(errorResponse.errorMessage)
				}
			}
		});
	}
	
	return {
		writeComment:writeComment,
		getCommentList:getCommentList,
		getComment:getComment,
		modifyComment:modifyComment,
		deleteComment:deleteComment
	};
	
})();