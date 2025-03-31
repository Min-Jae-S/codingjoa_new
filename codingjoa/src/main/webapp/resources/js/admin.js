let adminService = (function() {
	const contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	
	function getPagedUsers(adminUserCri, callback) {
		console.log("## getPagedUsers");
		console.log(JSON.stringify(adminUserCri, null, 2));
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/users`,
			data : adminUserCri,
			traditional : true,
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
				handleError(parseError(jqXHR));
			}
		});
	}

	function getPagedUsersBySearch(adminUserCri, callback) {
		console.log("## getPagedUsersBySearch");
		console.log(JSON.stringify(adminUserCri, null, 2));
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/users/`,
			data : adminUserCri,
			traditional : true,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function updateAdminUserInfo(userId, formData, callback) {
		console.log("## updateAdminUserInfo");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/info`,
			data : JSON.stringify(formData),
			contentType : "application/json; charset=utf-8",
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
				handleError(parseError(jqXHR));
			}
		});
	}

	function updateAdminUserAuth(userId, formData, callback) {
		console.log("## updateAdminUserAuth");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/auth`,
			data : JSON.stringify(formData),
			contentType : "application/json; charset=utf-8",
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function updateAdminUserPassword(userId, formData, callback) {
		console.log("## updateAdminUserPassword");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/password`,
			data : JSON.stringify(formData),
			contentType : "application/json; charset=utf-8",
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function getAdminUser(userId, callback) {
		console.log("## getAdminUser");
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/users/${userId}`,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function getPagedBoards(adminBoardCri, callback) {
		console.log("## getPagedBoards");
		console.log(JSON.stringify(adminBoardCri, null, 2));
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/boards`,
			data : adminBoardCri, // categories[]=4&categories[]=5 -> categories=4&categories=5
			traditional : true,
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
				handleError(parseError(jqXHR));
			}
		});
	}

	function getPagedBoardsBySearch(adminBoardCri, callback) {
		console.log("## getPagedBoardsBySearch");
		console.log(JSON.stringify(adminBoardCri, null, 2));
		$.ajax({
			type : "GET",
			url : `${contextPath}/api/admin/boards/`,
			data : adminBoardCri, // categories[]=4&categories[]=5 -> categories=4&categories=5
			traditional : true,
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
				handleError(parseError(jqXHR));
			}
		});
	}
	
	function deleteUsers(userIds, callback) {
		console.log("## deleteUsers");
		console.log(JSON.stringify(userIds, null, 2));
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/admin/users`,
			data : JSON.stringify(userIds),
			contentType : "application/json; charset=utf-8",
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
				handleError(parseError(jqXHR));
			}
		});
	}

	function deleteBoards(boardIds, callback) {
		console.log("## deleteBoards");
		console.log(JSON.stringify(boardIds, null, 2));
		$.ajax({
			type : "DELETE",
			url : `${contextPath}/api/admin/boards`,
			data : JSON.stringify(boardIds),
			contentType : "application/json; charset=utf-8",
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
				handleError(parseError(jqXHR));
			}
		});
	}

	return {
		getPagedUsers:getPagedUsers,
		getPagedUsersBySearch:getPagedUsersBySearch,
		updateAdminUserInfo:updateAdminUserInfo,
		updateAdminUserAuth:updateAdminUserAuth,
		updateAdminUserPassword:updateAdminUserPassword,
		getAdminUser:getAdminUser,
		getPagedBoards:getPagedBoards,
		getPagedBoardsBySearch:getPagedBoardsBySearch,
		deleteUsers:deleteUsers,
		deleteBoards:deleteBoards
	};
	
})();