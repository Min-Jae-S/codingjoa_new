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
	
	function updateNickname(userId, formData, callback) {
		console.log("## updateNickname");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/nickname`,
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
	
	function updateEmail(userId, formData, callback) {
		console.log("## updateEmail");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/email`,
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
	
	function updateAddr(userId, formData, callback) {
		console.log("## updateAddr");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/address`,
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
	
	function updateAgree(userId, formData, callback) {
		console.log("## updateAgree");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "PUT",
			url : `${contextPath}/api/admin/users/${userId}/agree`,
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
	
	function updatePassword(userId, formData, callback) {
		console.log("## updatePassword");
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

	function updateAuth(userId, formData, callback) {
		console.log("## updateAuth");
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
	
	function registerUser(formData, callback) {
		console.log("## registerUser");
		console.log(JSON.stringify(formData, null, 2));
		$.ajax({
			type : "POST",
			url : `${contextPath}/api/admin/users/register`,
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
	
	function getUser(userId, callback) {
		console.log("## getUser");
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
		updateNickname:updateNickname,
		updateEmail:updateEmail,
		updateAddr:updateAddr,
		updateAgree:updateAgree,
		updatePassword:updatePassword,
		updateAuth:updateAuth,
		registerUser:registerUser,
		getUser:getUser,
		getPagedBoards:getPagedBoards,
		getPagedBoardsBySearch:getPagedBoardsBySearch,
		deleteUsers:deleteUsers,
		deleteBoards:deleteBoards
	};
	
})();