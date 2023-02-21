console.log("## Reply module");

let replyService = (function() {
	
	function getReplyList(url, callback) {
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
	
	function writeReply(url, obj, callback) {
		$.ajax({
			type : "POST",
			url : url,
			data : JSON.stringify(obj),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			success : function(result) {
				callback(result);
			},
			error : function(e) {
				console.log(e.responseText);
			}
		});
	}
	
	return {getReplyList:getReplyList,
			writeReply:writeReply};
	
})();