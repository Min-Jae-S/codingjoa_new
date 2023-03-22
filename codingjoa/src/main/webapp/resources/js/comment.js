console.log("## Comment Module Ready");

let commentService = (function() {
	
	function writeComment(url, comment, callback) {
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				console.log(result);
				alert(result.message);
			},
			error : function(jqXHR) {
				callback(jqXHR);
				/*
				if (jqXHR.status == 401) {
					let errorMessage = JSON.parse(jqXHR.responseText).errorMessage;
					alert(errorMessage);
				}
				
				if (jqXHR.status == 422) {
					let errorMap = JSON.parse(jqXHR.responseText).errorMap;
					$.each(errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				}
				*/
			}
		});
	}
	
	function getCommentList(url, callback) {
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				callback(result);
			},
			error : function(jqXHR) {
				callback(jqXHR.responseText);
			}
		});
	}
	
	return {writeComment:writeComment,
			getCommentList:getCommentList};
	
})();