let adminService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getPagedBoards(page, recordCnt, callback) {
		console.log("## getPagedBoards");
		let url = `${contextPath}/api/admin/boards?page=${page}&recordCnt=${recordCnt}`;
		console.log("> URL = '%s'", url);
		
		$.ajax({
			type : "GET",
			url : url,
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
		getPagedBoards:getPagedBoards,
		deleteBoards:deleteBoards
	};
	
})();