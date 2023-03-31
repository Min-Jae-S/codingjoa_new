console.log("## Comment Service Ready");

let commentService = (function() {
	
	function writeComment(url, comment, callback) {
		console.log("## Write Comment");
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(errorResponse);
				
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
		console.log("## Request Comment List");
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log(result);
				callback(result);
			},
			error : function(jqXHR) {
				console.log(jqXHR);
			}
		});
	}

	function getComment(url, callback) {
		console.log("## Request Comment");
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log(result);
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(errorResponse);
				
				if (jqXHR.status == 401) {
					alert(errorResponse.errorMessage)
				} else if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert("오류가 발생하였습니다.");
				}
			}
		});
	}
	
	function deleteComment(url, callback) {
		console.log("## Delete Comment");
		$.ajax({
			type : "DELETE",
			url : url,
			dataType : "json",
			success : function(result) {
				console.log(result);
				callback(result);
			},
			error : function(jqXHR) {
				let errorResponse = JSON.parse(jqXHR.responseText);
				console.log(errorResponse);
				
				if (jqXHR.status == 401) {
					alert(errorResponse.errorMessage)
				} else if (jqXHR.status == 422) {
					$.each(errorResponse.errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				} else {
					alert("오류가 발생하였습니다.");
				}
			}
		});
	}
	
	return {
		writeComment:writeComment,
		getCommentList:getCommentList,
		getComment:getComment,
		deleteComment:deleteComment
	};
	
})();