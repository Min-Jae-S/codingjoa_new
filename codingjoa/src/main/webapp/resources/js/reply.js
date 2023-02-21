console.log("## Reply module");

let replyService = (function() {
	
	function getReplyList(url) {
		$.getJSON(url, function(data) {
			callback(data);
		});
	}
	
	return {getReplyList:getReplyList};
	
})();