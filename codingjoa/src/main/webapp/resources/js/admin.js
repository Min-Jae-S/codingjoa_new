let adminService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getBoards(page, recordCnt, callback) {
		console.log("## getBoards");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/boards`,
			data : {
				page : page,
				recordCnt : recordCnt
			},
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				let errorResponse = parseError(jqXHR);
				console.log(errorResponse);
			}
		});
	}

	function deleteBoards(boardIds, callback) {
		console.log("## deleteBoards");
		let url = `${contextPath}/api/admin/boards`;
		console.log("> URL = '%s'", url);
		console.log("> sendData = %s", JSON.stringify(boardIds, null, 2));
		
		$.ajax({
			type : "DELETE",
			url : url,
			data : JSON.stringify(boardIds),
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
				console.log(errorResponse);
			}
		});
	}

	return {
		getBoards:getBoards,
		deleteBoards:deleteBoards
	};
	
})();