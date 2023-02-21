console.log("## Reply module");

let replyService = (function() {
	
	function getReplyList(url, callback) {
		$.ajax({
			type : "GET",
			url : url,
			dataType : "json",
			success : function(result) {
				callback(result.data);
			},
			error : function(e) {
				console.log(e.responseText);
			}
		});
	}
	
	return {getReplyList:getReplyList};
	
})();