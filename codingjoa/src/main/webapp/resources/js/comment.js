console.log("## Comment service ready - comment.js");

let commentService = (function() {
	
	function writeComment(url, comment, callback) {
		console.log("## Write comment: %s", JSON.stringify(comment, null, 2));
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
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
					alert(errorResponse.errorMessage)
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
	
	function getCommentList(url, callback) {
		console.log("## Get comment list: %s", url);
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
			}
		});
	}

	function getComment(url, callback) {
		console.log("## Get comment...");
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
					alert(errorResponse.errorMessage)
				}
			}
		});
	}
	
	function modifyComment(url, comment, callback) {
		console.log("## Update comment...");
		
		$.ajax({
			type : "PATCH",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.table(JSON.stringify(result, null, 2));
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
					alert(errorResponse.errorMessage)
				}
			}
		});
	}
	
	function deleteComment(url, callback) {
		console.log("## Delete comment...");
		$.ajax({
			type : "DELETE",
			url : url,
			dataType : "json",
			success : function(result) {
				console.table(JSON.stringify(result, null, 2));
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