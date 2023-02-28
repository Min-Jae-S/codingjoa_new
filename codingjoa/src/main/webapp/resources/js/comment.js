console.log("## comment module");

let commentService = (function() {
	
	function writeComment(url, comment) {
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
			error : function(e) {
				console.log(e.responseText);
				
				if(e.status == 422) {
					var errorMap = JSON.parse(e.responseText).errorMap;
					
					$.each(errorMap, function(errorField, errorMessage) {
						alert(errorMessage);
					});
				}
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
			error : function(e) {
				console.log(e.responseText);
			}
		});
	}
	
	
	return {writeComment:writeComment,
			getCommentList:getCommentList};
	
})();