console.log("## comment module");

let commentService = (function() {
	
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
	
	function writeComment(url, comment, callback) {
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(comment),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				callback(result);
			},
			error : function(e) {
				callback(e);
				//console.log(e.responseText);
			}
		});
	}
	
	return {getCommentList:getCommentList,
			writeComment:writeComment};
	
})();