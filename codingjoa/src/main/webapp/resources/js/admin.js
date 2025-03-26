let adminService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getPagedUsers(adminUserCri, callback) {
		console.log("## getPagedUsers");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/users`,
			data : adminUserCri,
			traditional : true,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}

	function getPagedUsersBySearch(adminUserCri, callback) {
		console.log("## getPagedUsersBySearch");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/users/`,
			data : adminUserCri,
			traditional : true,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function getPagedBoards(adminBoardCri, callback) {
		console.log("## getPagedBoards");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/boards`,
			data : adminBoardCri, // categories[]=4&categories[]=5 -> categories=4&categories=5
			traditional : true,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}

	function getPagedBoardsBySearch(adminBoardCri, callback) {
		console.log("## getPagedBoardsBySearch");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/boards/`,
			data : adminBoardCri, // categories[]=4&categories[]=5 -> categories=4&categories=5
			traditional : true,
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function deleteUsers(userIds, callback) {
		console.log("## deleteUsers");
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/admin/users`,
			data : JSON.stringify(userIds),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}

	function deleteBoards(boardIds, callback) {
		console.log("## deleteBoards");
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/admin/boards`,
			data : JSON.stringify(boardIds),
			contentType : "application/json; charset=utf-8",
			dataType : "json",
			beforeSend : function(xhr, settings) {
				console.log("%c> BEFORE SEND", "color:blue");
				console.log(JSON.stringify(settings, ["type", "url", "contentType", "dataType", "data"], 2));
			},
			success : function(result) {
				console.log("%c> SUCCESS", "color:green");
				console.log(JSON.stringify(result, null, 2));
				callback(result);
			},
			error : function(jqXHR) {
				console.log("%c> ERROR", "color:red");
				handleError(parseError(jqXHR));
			}
		});
	}

	return {
		getPagedUsers:getPagedUsers,
		getPagedUsersBySearch:getPagedUsersBySearch,
		getPagedBoards:getPagedBoards,
		getPagedBoardsBySearch:getPagedBoardsBySearch,
		deleteUsers:deleteUsers,
		deleteBoards:deleteBoards
	};
	
})();